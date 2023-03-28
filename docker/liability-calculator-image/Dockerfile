###############################################################################
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
###############################################################################

###############################################################################
# Build Click Count Job
###############################################################################

FROM maven:3.8-eclipse-temurin-17 AS builder

# Get Liability Calculator job and compile it
COPY ./java/flink-liability-calculator /opt/flink-liability-calculator
WORKDIR /opt/flink-liability-calculator
RUN mvn clean verify

# WORKAROUND - If this is not working, try using this below to download the dependencies the first time. It's obviously not a great idea!
# RUN mvn clean verify -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true


###############################################################################
# Build Liability Calculator Image
###############################################################################

FROM apache/flink:1.16.0-scala_2.12-java11

WORKDIR /opt/flink/bin

# Copy Liability Calculator Job
COPY --from=builder /opt/flink-liability-calculator/target/flink-liability-calculator-*.jar /opt/flink/usrlib/LiabilityCalculator.jar

COPY ./conf /opt/flink/conf