export DEFECTS4J_HOME=~/defects4j
export PATH=$PATH:${DEFECTS4J_HOME}/framework/bin

echo $PATH

export bugId=1
export projId=Lang
export ProjDir=/tmp/"${projId}_${bugId}_buggy"

echo $ProjDir

if [ -d "$ProjDir" ]; then
  printf '%s\n' "Delete existing directory ($ProjDir)"
    rm -rf "$ProjDir"
fi

defects4j checkout -p $projId -v ${bugId}b -w $ProjDir

cd $ProjDir

echo Compiling code...

#TODO: write compile command
defects4j compile


echo Running Astor...

export ASTOR_HOME=~/astor
export ASTOR_JAR=${ASTOR_HOME}/target/astor-0.0.2-SNAPSHOT-jar-with-dependencies.jar

#configuration for each project
export Dependencies=${ASTOR_HOME}/examples/libs/junit-4.4.jar
export Package=org.apache.commons
export Output_Dir="${ProjDir}/output_astor"

#list of failing tests that trigger the bug
export FailingTests=org.apache.commons.math.analysis.solvers.BisectionSolverTest 

export ARGS="-dependencies ${Dependencies}"
export ARGS=${ARGS}" -mode statement"
export ARGS=${ARGS}" -failing ${FailingTests}"
export ARGS=${ARGS}" -location ${ProjDir}"
export ARGS=${ARGS}" -package ${Package}"
export ARGS=${ARGS}" -srcjavafolder /src/java/"
export ARGS=${ARGS}" -srctestfolder /src/test/"
export ARGS=${ARGS}" -binjavafolder /target/classes"
export ARGS=${ARGS}" -bintestfolder /target/tests"
export ARGS=${ARGS}" -javacompliancelevel 7"
export ARGS=${ARGS}" -out ${Output_Dir}"

#Astor's repair parameters
export ARGS=${ARGS}" -flthreshold 0.5"
export ARGS=${ARGS}" -scope local"
export ARGS=${ARGS}" -seed 10"
export ARGS=${ARGS}" -maxgen 50"
export ARGS=${ARGS}" -stopfirst true"
export ARGS=${ARGS}" -maxtime 100"
export ARGS=${ARGS}" -flthreshold 1"
export ARGS=${ARGS}" -stopfirst true"
export ARGS=${ARGS}" -loglevel INFO"
export ARGS=${ARGS}" -saveall true"
export ARGS=${ARGS}" -parameters logtestexecution:true:runexternalvalidator:true"

echo $ARGS

java  -cp "$ASTOR_JAR" fr.inria.main.evolution.AstorMain ${ARGS}

echo ------ Finish ----

