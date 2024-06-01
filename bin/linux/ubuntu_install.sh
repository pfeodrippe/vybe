# Install desktop (for UTM)
sudo apt update
sudo apt install ubuntu-desktop

# Install agents (for UTM)
sudo apt update
sudo apt install qemu-guest-agent rlwrap

# Add sharing (for UTM)
sudo mkdir -p /Users/pfeodrippe/dev
sudo mount -t 9p -o trans=virtio share /Users/pfeodrippe/de
sudo chown -R $USER /Users/pfeodrippe/dev/vybe/raylib/src/
sudo chown -R $USER /Users/pfeodrippe/dev/vybe/bin/
sudo chown -R $USER /Users/pfeodrippe/dev/vybe/native/

# Install deps
sudo apt update
sudo apt install make rlwrap build-essential libwayland-dev libxkbcommon-dev \
     libx11-dev libxcursor-dev libgl1-mesa-dev xorg-dev

# Install JDK
curl -s "https://get.sdkman.io" | bash
source "/home/pfeodrippe/.sdkman/bin/sdkman-init.sh"
sdk install java 22-open

# Extract
VYBE_JEXTRACT=/Users/pfeodrippe/dev/jextract-linux/bin/jextract \
    VYBE_EXTENSION=so \
    VYBE_GCC=gcc \
    bin/jextract-libs.sh

# Install clojure
curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
chmod +x linux-install.sh
sudo ./linux-install.sh

# Run game
# clj -M:dev -m vybe.native.loader && clj -M:dev -m vybe.raylib
