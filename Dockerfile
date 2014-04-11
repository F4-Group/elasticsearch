FROM dockerfile/java
MAINTAINER F4 <dev@f4-group.com>

# Add community-maintained universe repository to sources
RUN sed -i.bak 's/main$/main universe/' /etc/apt/sources.list

# Install dependencies
RUN apt-get update
RUN apt-get install -y maven

# Add sources to container
ADD . /tmp/elasticsearch
WORKDIR /tmp/elasticsearch

# Build Elasticsearch
RUN mvn clean package -DskipTests

# Install Elasticsearch
RUN tar xzf target/releases/elasticsearch-1.*+f4.tar.gz
RUN mv elasticsearch-1.*+f4 /opt/elasticsearch
RUN rm -r /tmp/elasticsearch 

# Expose ports.
#   - 9200: HTTP
#   - 9300: transport
EXPOSE 9200
EXPOSE 9300

# Define an entry point.
ENTRYPOINT ["/opt/elasticsearch/bin/elasticsearch"]