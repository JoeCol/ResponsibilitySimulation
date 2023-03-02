

javac ResponsibilityGUI.java
DIRT_INTERVAL[0]=400
DIRT_INTERVAL[1]=300
DIRT_INTERVAL[2]=150

SAVE_LOC_N="/home/joe/DirtExperiment/Naive/Result/Dirt"
SAVE_LOC_R="/home/joe/DirtExperiment/ResAgents/Result/Dirt"
SIM_STEPS=20000

for j in {1..20}
do
    for i in 0 1 2
    do
        RESULTS_N=$SAVE_LOC_N${DIRT_INTERVAL[$i]}"/"
        RESULTS_R=$SAVE_LOC_R${DIRT_INTERVAL[$i]}"/"
        java ResponsibilityGUI naive saveLoc $RESULTS_N simSteps $SIM_STEPS dirtInterval ${DIRT_INTERVAL[$i]} 10 speed 0
        java ResponsibilityGUI saveLoc $RESULTS_R simSteps $SIM_STEPS dirtInterval ${DIRT_INTERVAL[$i]} 10 speed 0
    done
done