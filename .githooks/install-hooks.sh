#!/usr/bin/env bash
# install-hooks.sh – Symlink the project's Git hooks into .git/hooks/.
#
# Run this script once after cloning the repository to activate the
# pre-commit accessibility checks provided by A11yAgent.
#
# Usage:
#   bash .githooks/install-hooks.sh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
GIT_HOOKS_DIR="$REPO_ROOT/.git/hooks"

if [[ ! -d "$GIT_HOOKS_DIR" ]]; then
    echo "install-hooks: .git/hooks directory not found at $GIT_HOOKS_DIR"
    echo "  Make sure you are running this script from within the repository."
    exit 1
fi

install_hook() {
    local name="$1"
    local source="$SCRIPT_DIR/$name"
    local target="$GIT_HOOKS_DIR/$name"

    if [[ ! -f "$source" ]]; then
        echo "install-hooks: source hook not found: $source"
        return 1
    fi

    # Make the source hook executable.
    chmod +x "$source"

    # Back up any existing hook that is not already our symlink.
    if [[ -e "$target" && ! -L "$target" ]]; then
        local backup="$target.bak"
        echo "install-hooks: backing up existing $name hook to $target.bak"
        mv "$target" "$backup"
    elif [[ -L "$target" ]]; then
        # Remove a stale symlink so we can recreate it cleanly.
        rm "$target"
    fi

    ln -s "$source" "$target"
    echo "install-hooks: installed $name  →  $target"
}

install_hook "pre-commit"

echo ""
echo "Git hooks installed successfully."
echo ""
echo "The pre-commit hook will run A11yAgent accessibility checks on staged"
echo "Kotlin files before each commit."
echo ""
echo "To build the A11yAgent JAR (required by the hook):"
echo "  ./gradlew :A11yAgent:shadowJar"
echo ""
echo "To skip the hook for a single commit:"
echo "  git commit --no-verify"
