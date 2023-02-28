

javac ResponsibilityGUI.java
DIRT_INTERVAL[0]=15
DIRT_INTERVAL[1]=10
DIRT_INTERVAL[2]=5

SAVE_LOC="/Users/user/Library/CloudStorage/OneDrive-TheUniversityofManchester/Manchester/Responsibility/DirtExperiment/Naive/Result/Dirt"
SIM_STEPS=5000

for j in {1..20}
do
    for i in 0 1 2
    do
        RESULTS=$SAVE_LOC${DIRT_INTERVAL[$i]}"/"
        java ResponsibilityGUI naive saveLoc $RESULTS simSteps $SIM_STEPS dirtInterval ${DIRT_INTERVAL[$i]} 5
    done
done

SAVE_LOC="/Users/user/Library/CloudStorage/OneDrive-TheUniversityofManchester/Manchester/Responsibility/DirtExperiment/ResAgents/Result/Dirt"
for j in {1..20}
do
    for i in 0 1 2
    do
        RESULTS=$SAVE_LOC${DIRT_INTERVAL[$i]}"/"
        java ResponsibilityGUI saveLoc $RESULTS simSteps $SIM_STEPS dirtInterval ${DIRT_INTERVAL[$i]} 5
    done
done