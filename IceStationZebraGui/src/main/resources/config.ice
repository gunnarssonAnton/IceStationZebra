{
  "global": 99,
  "user": "name",
  "events": [
    {
      "dockerImage": "ubuntu:latest",
      "givenName": "gcc",
      "compileCommand": "gcc FILES -o OUTPUT",
      "installation": [
        "apt update && apt upgrade -y",
        "apt install gcc -y",
        "gcc --version"
      ]
    },
    {
      "dockerImage": "ubuntu:latest",
      "givenName": "clang",
      "compileCommand": "clang FILES -o OUTPUT",
      "installation": [
        "apt update && apt upgrade -y",
        "apt install clang -y",
        "clang --version"
      ]
    },
    {
      "dockerImage": "ubuntu:latest",
      "givenName": "openJavac",
      "compileCommand": "javac -d OUTPUT FILES",
      "installation": [
        "apt update && apt upgrade -y",
        "apt install openjdk-17-jdk -y",
        "javac --version"
      ]
    },
    {
      "dockerImage": "ubuntu:latest",
      "givenName": "oracleAidsJavac",
      "compileCommand": "javac -d OUTPUT FILES",
      "installation": [
        "apt update && apt upgrade -y",
        "cp /files/jdk-17_linux-aarch64_bin.tar /jdk-17_linux-aarch64_bin.tar",
        "mkdir -p /usr/lib/jvm",
        "tar -xvf jdk-17_linux-aarch64_bin.tar -C /usr/lib/jvm",
        "update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-17.0.10/bin/java 100",
        "update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk-17.0.10/bin/javac 100",
        "update-alternatives --config java",
        "update-alternatives --config javac",
        "echo 'export JAVA_HOME=/usr/lib/jvm/jdk-17.0.10' >> ~/.bashrc",
        "echo 'export PATH=\\$PATH:\\$JAVA_HOME/bin' >> ~/.bashrc",
        "java -version",
        "javac -version"
      ]
    },
    {
      "dockerImage": "ubuntu:latest",
      "givenName": "ECJ",
      "compileCommand": "NO COMMAND",
      "installation": [
        "apt-get update && apt-get upgrade -y",
        "apt-get install ecj -y",
        "apt-get install --reinstall java-wrapper -y",
        "ecj --version"
      ]
    },

    {
          "dockerImage": "ubuntu:latest",
          "givenName": "janino",
          "compileCommand": "javac -cp LIB_DIR/* -d OUTPUT FILES",
          "installation": [
          "ls /",
            "apt update && apt upgrade -y",
            "apt install default-jdk -y",
            "mkdir LIB_DIR",
            "cp -v /files/janino-3.1.9.jar LIB_DIR/janino-3.1.9.jar",
            "cp /files/commons-compiler-3.1.9.jar LIB_DIR/commons-compiler-3.1.9.jar"
          ]
        }
  ]
}