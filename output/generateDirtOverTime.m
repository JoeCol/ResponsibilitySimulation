legendStr = ["0.025% Chance of Dirt Every Step",
             "0.02% Chance of Dirt Every Step",
             "0.016% Chance of Dirt Every Step",
             "0.014% Chance of Dirt Every Step",
             "0.0125% Chance of Dirt Every Step"];

figure
hold on
title("Average Number of Dirty Tiles (Naive Agents)");
xlabel("Time");
ylabel("Number of Dirty Tiles");
ylim([0 25]);
for cn = 1:size(dirtAppearence,2)
    tmp = naive(naive(:,2)==dirtAppearence(cn),[1,4]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    plot(((mean(toShow,2)./possibleDirtLocations)*100),'DisplayName',legendStr(cn));
end
legend;
hold off


figure
hold on
title("Average Number of Dirty Tiles (Responsibility Agents)");
xlabel("Time");
ylabel("Number of Dirty Tiles");
ylim([0 25]);
for cn = 1:size(dirtAppearence,2)
    tmp = res(res(:,2)==dirtAppearence(cn),[1,4]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    plot(((mean(toShow,2)./possibleDirtLocations)*100),'DisplayName',legendStr(cn));
end
legend;
hold off
