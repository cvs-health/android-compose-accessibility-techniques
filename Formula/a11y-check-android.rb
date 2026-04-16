class A11yCheckAndroid < Formula
  desc "Static accessibility checker for Jetpack Compose (WCAG 2.2)"
  homepage "https://github.com/cvs-health/android-compose-accessibility-techniques"
  version "0.1.0"
  license "Apache-2.0"

  url "https://github.com/cvs-health/android-compose-accessibility-techniques/releases/download/v#{version}/a11y-check-android-#{version}.jar"
  # To update sha256: shasum -a 256 A11yAgent/build/libs/a11y-check-android-0.1.0.jar
  sha256 "PLACEHOLDER_UPDATE_ON_RELEASE"

  depends_on "openjdk@21"

  def install
    libexec.install "a11y-check-android-#{version}.jar"

    (bin/"a11y-check-android").write <<~EOS
      #!/bin/bash
      exec "#{Formula["openjdk@21"].opt_bin}/java" -jar "#{libexec}/a11y-check-android-#{version}.jar" "$@"
    EOS
  end

  test do
    assert_match "Available rules", shell_output("#{bin}/a11y-check-android --list-rules")
  end
end
