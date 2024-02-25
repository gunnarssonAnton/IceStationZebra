# Ice Station Zebra Documentation

## Introduction

Ice Station Zebra is a tool designed for developers to compile and execute code across various compilers and configurations within isolated Docker environments. This documentation provides you with all you need to get started, including installation, configuration, and execution.

## Installation

### Prerequisites

- Java 21 runtime and JDK
- Docker

### Steps

1. **Download Ice Station Zebra**: Download the latest version from [Ice Station Zebra Releases](#).

2. **Install Java**: Ensure Java 21 runtime and JDK are installed on your system.

    ```bash
    sudo apt-get install openjdk-21-jdk
    ```

3. **Install Docker**: Follow the Docker installation guide for your operating system on [Docker's Official Documentation](https://docs.docker.com/get-docker/).

4. **Set up Ice Station Zebra**: Unzip the downloaded file and run the installer.

    ```bash
    unzip ice-station-zebra.zip
    cd ice-station-zebra
    ./install.sh
    ```

## Basic Usage

### Configuring Events

1. **Launch Ice Station Zebra** and navigate to the *Events* section.

2. **Create a New Event**: Specify the event name, select the operating system, compiler, and any necessary dependencies.

    ```markdown
    - **Event Name**: `MyEvent`
    - **Operating System**: Ubuntu
    - **Compiler**: GCC
    - **Dependencies**: libstdc++6
    ```

3. **Save the Event** for later use.

### Compiling Code

1. **Select an Event** you've created from the *Events* list.

2. **Choose the Codebase** you wish to compile by navigating to the *Codebase* section and selecting the folder containing your source code.

3. **Compile**: Click on the *Compile* button to start the compilation process. You will see a success or error message based on the outcome.

### Executing Compiled Code

1. **Navigate to the Compiled Outputs**: After successful compilation, go to the *Output* section to see your compiled executables.

2. **Execute**: Select an executable and click on *Execute*. Optionally, you can specify pre-execution and post-execution scripts before running the executable.

    ```markdown
    - **Pre-Execution Script**: `before.sh` (optional)
    - **Post-Execution Script**: `after.sh` (optional)
    ```

3. **View Results**: The output and any execution logs will be displayed upon completion.

## Additional Resources

- **FAQs and Troubleshooting**: Visit our [FAQs section](#) for answers to common questions and troubleshooting tips.

- **Community and Support**: Join our [developer community](#) for support and discussions.

- **Contributing**: Interested in contributing? Check out our [contribution guidelines](#).

For more detailed information on each feature, please refer to the user manual included in the installation package or visit our official documentation website.
