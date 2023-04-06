rm *.class
javac ResponsibilityGUI.java

SAVE_LOC_N="/Users/user/Library/CloudStorage/OneDrive-TheUniversityofManchester/Manchester/Responsibility/DirtExperiment2/Naive/Result/Dirt"
SAVE_LOC_R="/Users/user/Library/CloudStorage/OneDrive-TheUniversityofManchester/Manchester/Responsibility/DirtExperiment2/ResAgents/Result/Dirt"
SIM_STEPS=20000

rdirtrate[1]=40
rdirtrate[2]=50
rdirtrate[3]=60
rdirtrate[4]=70
rdirtrate[5]=80

bdirtrate[1]=1000
bdirtrate[2]=1250
bdirtrate[3]=1500
bdirtrate[4]=1750
bdirtrate[5]=2000

TOTAL_RUNS=$((5*500))
RUN=0
for dirt in {1..5}
do
    for j in {1..500}
    do
        RUN=$(($RUN+1))
        FILE_TO_CHECK=$SAVE_LOC_N$dirt"/DirtLevels_"$j".csv"
        if ! test -f $FILE_TO_CHECK; then
            RESULTS_N=$SAVE_LOC_N$dirt"/"
            java ResponsibilityGUI naive saveLoc $RESULTS_N simSteps $SIM_STEPS dirtInterval ${rdirtrate[$dirt]} ${bdirtrate[$dirt]} speed 0 nogui&
            
        fi

        FILE_TO_CHECK=$SAVE_LOC_R$dirt"/DirtLevels_"$j".csv"
        if ! test -f $FILE_TO_CHECK; then
            RESULTS_R=$SAVE_LOC_R$dirt"/"
            java ResponsibilityGUI saveLoc $RESULTS_R simSteps $SIM_STEPS dirtInterval ${rdirtrate[$dirt]} ${bdirtrate[$dirt]} speed 0 nogui& 
                   
        fi
        wait
        echo -ne $RUN of $TOTAL_RUNS done\\r
    done
done