{
  "global": 99,
  "user": "name",
  "events": [
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
      "compileCommand": "java -jar /files/org.eclipse.jdt.core.compiler.batch_3.37.0.v20240215-1558.jar -source 1.8 FILES -d OUTPUT",
      "installation": [
        "apt-get update",
        "apt-get install -y software-properties-common",
        "add-apt-repository ppa:openjdk-r/ppa",
        "apt-get update && apt-get upgrade -y",
        "apt-get install openjdk-17-jdk -y"
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
        },
    {
          "dockerImage": "ubuntu:latest",
          "givenName": "jbg",
          "compileCommand": "javac -cp LIB_DIR/* -d OUTPUT FILES",
          "installation": [
            "apt update -y",
            "apt upgrade -y",
            "apt-get update -y",
            "apt-get upgrade -y",
            "apt-get install openjdk-17-jdk -y",
            "apt install git -y",
            "git --version",
            "git clone https://github.com/Elzawawy/java-bytecode-generator.git",
            "apt install make -y",
            "apt-get install bison -y",
            "apt-get install flex -y",
            "apt-get install g++ -y",
            "cd java-bytecode-generator",
            "ls"
          ]
        },
    {
          "dockerImage": "ubuntu:latest",
          "givenName": "yakout",
          "compileCommand": "javac -cp LIB_DIR/* -d OUTPUT FILES",
          "installation": [
            "apt update -y",
            "apt upgrade -y",
            "apt install git -y",
            "git --version",
            "git clone https://github.com/yakout/compiler.git",
            "ls"
          ]
        },
    {
          "dockerImage": "ubuntu:latest",
          "givenName": "hoc",
          "compileCommand": "java -jar /files/HouseOfCompiler.jar -jar FILES [out-dir=OUTPUT]",
          "installation": [
            "apt-get update -y",
            "apt-get upgrade -y",
            "apt-get install openjdk-17-jdk -y",
            "apt install nano -y"
          ]
        },
    {
          "dockerImage": "ubuntu:latest",
          "givenName": "jcc",
          "compileCommand": "mvn exec:java -Dexec.mainClass=com.sky.jcc.compiler.CodeGenerationMain -Dexec.args=RecursiveSum.java",
          "installation": [
            "apt-get update -y",
            "apt-get upgrade -y",
            "apt-get install openjdk-17-jdk -y",
            "apt install maven -y",
            "apt install nano -y"
          ]
        },
    {
          "dockerImage": "ubuntu:latest",
          "givenName": "gcj",
          "compileCommand": "",
          "installation": [
            "apt update -y",
            "apt upgrade -y",
            "apt install gcc -y",
            "apt install build-essential -y"
          ]
        }
  ]
}