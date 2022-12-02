package ru.ibsqa.qualit.steps;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Управление условием выполнения шагов
 */
@Component
public class StepFlowImpl implements IStepFlow {

    @Autowired
    private CoreUtilSteps coreUtilSteps;

    private ThreadLocal<List<Boolean>> blocksStorage = new InheritableThreadLocal<>();

    @Synchronized
    private List<Boolean> getBlocks() {
        if (null == blocksStorage.get()) {
            blocksStorage.set(new ArrayList<>());
        }
        return blocksStorage.get();
    }

    @Override
    @Synchronized
    public boolean isIgnore() {
        return getBlocks().stream().anyMatch(item->item == false);
    }

    @Override
    @Synchronized
    public void createBlock(boolean active) {
        List<Boolean> blocks = getBlocks();
        blocks.add(active);
        blocksStorage.set(blocks);
    }

    @Override
    @Synchronized
    public void inverseBlock() {
        List<Boolean> blocks = getBlocks();
        Boolean active = blocks.get(blocks.size()-1);
        active = !active;
        blocks.set(blocks.size()-1, active);
        blocksStorage.set(blocks);
    }

    @Override
    @Synchronized
    public void completeBlock() {
        List<Boolean> blocks = getBlocks();
        if (blocks.size()>0) {
            blocks.remove(blocks.size()-1);
        }
        blocksStorage.set(blocks);
    }

    @Override
    public void checkStepFlow(Runnable stepAction) {
        if (isIgnore()) {
            coreUtilSteps.stepIgnore();
        } else {
            stepAction.run();
        }
    }

    @Override
    public boolean prepareFlowStep() {
        if (isIgnore()) {
            createBlock(false);
            return false;
        }
        return true;
    }

}
