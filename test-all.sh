#!/bin/bash

# 1.6.0-SNAPSHOT needs to be mvn installed since it is not deployed
# yet and there is a failure on the mapreduce
#accumulo_versions=("1.5.0" "1.5.1" "1.6.0-SNAPSHOT")
accumulo_versions=("1.5.0" "1.5.1")

echo "Running some tests against version ${accumulo_versions[*]}"

mvn_test() {
  echo "Running mvn -Daccumulo.version=${1} clean test"
  mvn -Daccumulo.version=$1 clean test
}

mvn_mapreduce() {
  echo "Running mvn -Daccumulo.version=${1} compile exec:exec -Pmapreduce"
  mvn -Daccumulo.version=$1 compile exec:exec -Pmapreduce
}


mvn_shell() {
   echo "Running mvn -Daccumulo.version=${1} compile exec:exec -Pshell"
  cmds=$(cat <<-EOF
tables
createtable junk
insert "a" "b" "c" "d"
scan
exit
EOF
)
   echo "${cmds}" | mvn -Daccumulo.version=$1 compile exec:exec -Pshell
}

for version in "${accumulo_versions[@]}"; do
  echo "============================================"
  echo "Testing version ${version}"
  echo "============================================"
  # assuming running from the root directory
  mvn_test $version || exit 1
  mvn_mapreduce $version || exit 2
  mvn_shell $version || exit 3
  echo "============================================"
  echo "Testing version ${version} complete"
  echo "============================================"
  echo
done

echo "Congrats, everything passed against version ${accumulo_versions[*]}"
