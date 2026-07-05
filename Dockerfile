FROM ubuntu:latest
LABEL authors="akhma"

ENTRYPOINT ["top", "-b"]