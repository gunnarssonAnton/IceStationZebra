FROM ubuntu:latest

VOLUME /scripts
VOLUME /installs
VOLUME /compile_commands
VOLUME /codebase
VOLUME /output

ENV COMPILER_NAME==unset
ENV COMPILER_COMMAND==unset

COPY /scripts/compilation_entrypoint.sh /compilation_entrypoint.sh
RUN chmod +x /compilation_entrypoint.sh

CMD echo "$(ls)"


ENTRYPOINT /bin/bash