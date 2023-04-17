figure
hold on
title("Average Dirt Levels over Time (Naive No Check)");
xlabel("Time");
ylabel("Dirt Level");
ylim([0 25]);
titleT = 'Scenario %d';
for cn = 1:size(dirtAppearence,2)
    titleN = sprintf(titleT,dirtAppearence(cn));
    tmp = naive(naive(:,2)==dirtAppearence(cn),[1,4]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    plot(((mean(toShow,2)./possibleDirtLocations)*100),'DisplayName',titleN);
end
legend;
hold off

figure
hold on
title("Average Bad Dirt Levels over Time (Naive No Check)");
xlabel("Time");
ylabel("Dirt Level");
ylim([0 1]);
for cn = 1:size(dirtAppearence,2)
    titleN = sprintf(titleT,dirtAppearence(cn));
    tmp = naive(naive(:,2)==dirtAppearence(cn),[1,5]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    plot(((mean(toShow,2)./possibleDirtLocations)*100),'DisplayName',titleN);
end
legend;
hold off

figure
hold on
title("Average Dirt Levels over Time (Res)");
xlabel("Time");
ylabel("Dirt Level");
titleT = 'Scenario %d';
ylim([0 25]);
for cn = 1:size(dirtAppearence,2)
    titleN = sprintf(titleT,dirtAppearence(cn));
    tmp = res(res(:,2)==dirtAppearence(cn),[1,4]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    plot(((mean(toShow,2)./possibleDirtLocations)*100),'DisplayName',titleN);
end
legend;
hold off

figure
hold on
title("Average Bad Dirt Levels over Time (Res)");
xlabel("Time");
ylabel("Dirt Level");
ylim([0 1]);
legend;
for cn = 1:size(dirtAppearence,2)
    titleN = sprintf(titleT,dirtAppearence(cn));
    tmp = res(res(:,2)==dirtAppearence(cn),[1,5]);
    toShow = zeros(numOfSimSteps,1);
    for run = 1:numOfRuns
        toShow(:,run) = tmp(tmp(:,1)==run,2);
    end
    plot(((mean(toShow,2)./possibleDirtLocations)*100),'DisplayName',titleN);
end
hold off
