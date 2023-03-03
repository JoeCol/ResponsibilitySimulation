

javac ResponsibilityGUI.java

SAVE_LOC_N="/Users/user/Library/CloudStorage/OneDrive-TheUniversityofManchester/Manchester/Responsibility/DirtExperiment/Naive/Result/Dirt"
SAVE_LOC_R="/Users/user/Library/CloudStorage/OneDrive-TheUniversityofManchester/Manchester/Responsibility/DirtExperiment/ResAgents/Result/Dirt"
SIM_STEPS=20000

TOTAL_RUNS=$((201*20))
RUN=0
for dirt in {400..200}
do
    for j in {1..20}
    do
        RUN=$(($RUN+1))
        FILE_TO_CHECK=$SAVE_LOC_N$dirt"/DirtLevels_"$j".csv"
        echo $FILE_TO_CHECK
        if test -f $FILE_TO_CHECK; then
            echo exists
        else
            echo does not exist
            RESULTS_N=$SAVE_LOC_N$dirt"/"
            RESULTS_R=$SAVE_LOC_R$dirt"/"
            java ResponsibilityGUI naive saveLoc $RESULTS_N simSteps $SIM_STEPS dirtInterval $dirt 10 speed 0 nogui&
            java ResponsibilityGUI saveLoc $RESULTS_R simSteps $SIM_STEPS dirtInterval $dirt 10 speed 0 nogui&
            wait
        fi
        echo $RUN of $TOTAL_RUNS done
    done
done