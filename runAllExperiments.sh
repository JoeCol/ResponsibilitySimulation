find . -name "*.class" -type f -delete
javac ResponsibilityGUI.java

SAVE_LOC="/Users/user/Library/CloudStorage/OneDrive-TheUniversityofManchester/Manchester/Responsibility/JAAMASResults/Cleaning/"
SIM_STEPS=5000

rdirtrate[1]=1
rdirtrate[2]=2
rdirtrate[3]=4
rdirtrate[4]=7
rdirtrate[5]=10

bdirtrate[1]=0
bdirtrate[2]=10
bdirtrate[3]=25
bdirtrate[4]=50

TOTAL_RUNS=$((5*4*500))
RUN=0
for dirt in {1..5}
do
    for bdirt in {1..4}
    do
        for j in {1..500}
        do
            RUN=$(($RUN+1))
            RESULTS_LOCA=$SAVE_LOC"Dirt"$dirt"/SCDirt"$bdirt"/"
            FILE_TO_CHECK=$RESULTS_LOCA"DirtRecord_"$j".csv"
            RESULT_OUTPUT=$RESULTS_LOCA"Output_"$j".csv"
            #mkdir -p $RESULTS_LOCA
            if ! test -f $FILE_TO_CHECK; then
                java -Djava.awt.headless=true ResponsibilityGUI nogui simsteps $SIM_STEPS cleaning saveLocation $RESULTS_LOCA worldFile 14Rooms.world dirtChance ${rdirtrate[$dirt]} scdirtChance ${bdirtrate[$bdirt]}&
            fi
            wait
            echo -ne $RUN of $TOTAL_RUNS done\\r
        done
    done
done