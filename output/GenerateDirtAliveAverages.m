%Generate Dirt Alive averages

%Overall dirt appearance and runs - naive
names = {'Overall';'Regular';'Bad'};
naiveDirtAliveAverages = zeros(5,3);
resDirtAliveAverages = zeros(5,3);


for di = 1:size(dirtAppearence,2)
    naiveBadMean = mean(naiveDirtAlive(naiveDirtAlive(:,3)==2 & naiveDirtAlive(:,2)==dirtAppearence(di),4));
    resBadMean = mean(resDirtAlive(resDirtAlive(:,3)==2 & resDirtAlive(:,2)==dirtAppearence(di),4));

    naiveRegMean = mean(naiveDirtAlive(naiveDirtAlive(:,3)==1 & naiveDirtAlive(:,2)==dirtAppearence(di),4));
    resRegMean = mean(resDirtAlive(resDirtAlive(:,3)==1 & resDirtAlive(:,2)==dirtAppearence(di),4));

    naiveMean = mean(naiveDirtAlive(naiveDirtAlive(:,2)==dirtAppearence(di),4));
    resMean = mean(resDirtAlive(resDirtAlive(:,2)==dirtAppearence(di),4));

    naiveDirtAliveAverages(di,:) = [naiveMean,naiveRegMean,naiveBadMean];
    resDirtAliveAverages(di,:) = [resMean,resRegMean,resBadMean];
end

figure
hold on;
title("Average Dirt Alive - Naive");
bar(naiveDirtAliveAverages);
legend(names);
ylabel("Average Time Until Cleaned")
xlabel("Scenario")
xticks(dirtAppearence)
ylim([0,2500])
hold off;

%Overall dirt appearance and runs - res
figure
hold on;
title("Average Dirt Alive - Res")
bar(resDirtAliveAverages);
legend(names)
ylabel("Average Time Until Cleaned")
xlabel("Scenario")
xticks(dirtAppearence)
ylim([0,2500])
hold off;
