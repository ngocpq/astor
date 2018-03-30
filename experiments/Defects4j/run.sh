export DEFECTS4J_HOME=/home/horeng/defects4j
export PATH=$PATH:${DEFECTS4J_HOME}/framework/bin

echo $PATH

export bugId=1
export projId=Lang
export outDir=/tmp/"${projId}_${bugId}_buggy"

echo $outDir

defects4j checkout -p $projId -v ${bugId}b -w $outDir

cd $outDir

echo Compiling code...

#TODO: write compile command
defects4j compile


echo Running Astor...

export ASTOR_HOME=~/astor
export ASTOR_JAR=${ASTOR_HOME}/target/astor-0.0.2-SNAPSHOT-jar-with-dependencies.jar

java  -cp "$ASTOR_JAR" fr.inria.main.evolution.AstorMain 

echo ------ Finish ----

