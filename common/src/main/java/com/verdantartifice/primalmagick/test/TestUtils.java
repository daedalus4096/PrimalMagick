package com.verdantartifice.primalmagick.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.gametest.GameTestHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TestUtils {
    public static final String DEFAULT_BATCH = "defaultBatch";
    public static final String DEFAULT_TEMPLATE = "primalmagick:test/empty3x3x3";
    
    @Nullable
    private static String findPrefix(int stackDepth) {
        String callerClassName = StackWalker.getInstance().walk(s -> s.skip(stackDepth).findFirst()).map(f -> f.getClassName()).orElse(null);
        if (callerClassName != null) {
            try {
                Class<?> callerClass = Class.forName(callerClassName);
                GameTestHolder holder = callerClass.getAnnotation(GameTestHolder.class);
                if (holder != null && !holder.value().isEmpty()) {
                    return holder.value();
                }
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }
    
    private static String makeName(@Nullable String prefix, String group, String suffix) {
        return (prefix == null) ? String.join(".", group, suffix) : String.join(".", prefix, group, suffix);
    }
    
    private static String makeBatch(@Nullable String prefix, String batchName) {
        if (DEFAULT_BATCH.equals(batchName)) {
            return (prefix == null) ? batchName : prefix;
        } else {
            return (prefix == null) ? batchName : String.join(".", prefix, batchName);
        }
    }
    
    /**
     * Transforms the given consumer into a TestFunction that can be run via the Minecraft game test framework with
     * default options. <strong>This method is expected to be run from a class registered via the GameTestHolder 
     * annotation, rather than through the RegisterGameTestsEvent event. It will behave unpredictably otherwise.</strong>
     * 
     * @param generatedGroupName the method name of the calling test generator; used to compose the generated test name
     * @param nameSuffix the unique suffix to be appended to the name of the generated test
     * @param consumer the consumer to be transformed into a test function
     * @return the final TestFunction to be run in the Minecraft game test framework
     */
    public static TestFunction createTestFunction(String generatedGroupName, String nameSuffix, Consumer<GameTestHelper> consumer) {
        return createTestFunctionInner(findPrefix(2), generatedGroupName, nameSuffix, TestOptions.DEFAULT, consumer);
    }
    
    /**
     * Transforms the given consumer into a TestFunction that can be run via the Minecraft game test framework.
     * <strong>This method is expected to be run from a class registered via the GameTestHolder annotation, rather
     * than through the RegisterGameTestsEvent event. It will behave unpredictably otherwise.</strong>
     * 
     * @param generatedGroupName the method name of the calling test generator; used to compose the generated test name
     * @param nameSuffix the unique suffix to be appended to the name of the generated test
     * @param options any options to be applied to the generated test; see the GameTest annotation for details
     * @param consumer the consumer to be transformed into a test function
     * @return the final TestFunction to be run in the Minecraft game test framework
     */
    public static TestFunction createTestFunction(String generatedGroupName, String nameSuffix, TestOptions options, Consumer<GameTestHelper> consumer) {
        return createTestFunctionInner(findPrefix(2), generatedGroupName, nameSuffix, options, consumer);
    }
    
    private static TestFunction createTestFunctionInner(String prefix, String generatedGroupName, String nameSuffix, TestOptions options, Consumer<GameTestHelper> consumer) {
        String finalName = makeName(prefix, generatedGroupName, nameSuffix);
        String batch = makeBatch(prefix, options.batch());
        return new TestFunction(
            batch,
            finalName,
            options.template(),
            StructureUtils.getRotationForRotationSteps(options.rotationSteps()),
            options.timeoutTicks(),
            options.setupTicks(),
            options.required(),
            options.manualOnly(),
            options.requiredSuccesses(),
            options.attempts(),
            options.skyAccess(),
            consumer
        );
    }
    
    /**
     * Transforms the given bi-consumer into a collection of TestFunction objects that can be run via the Minecraft game test
     * framework with default options. One TestFunction is created for each entry in the given {@code params} map, with the key 
     * being the suffix of the generated test function name and the value being the parameter passed into the given bi-consumer.
     * <strong>This method is expected to be run from a class registered via the GameTestHolder annotation, rather than through
     * the RegisterGameTestsEvent event. It will behave unpredictably otherwise.</strong>
     * 
     * @param <T> the type of parameter to be accepted by the given test bi-consumer
     * @param generatedGroupName the method name of the calling test generator; used to compose the generated test name
     * @param params a map of test name suffixes to test parameter values
     * @param consumer the bi-consumer to be transformed into a test function collection
     * @return the final collection of TestFunction objects, to be run in the Minecraft game test framework
     */
    public static <T> Collection<TestFunction> createParameterizedTestFunctions(String generatedGroupName, Map<String, T> params, BiConsumer<GameTestHelper, T> consumer) {
        return createParameterizedTestFunctionsInner(findPrefix(2), generatedGroupName, TestOptions.DEFAULT, params, consumer);
    }
    
    /**
     * Transforms the given bi-consumer into a collection of TestFunction objects that can be run via the Minecraft game test
     * framework. One TestFunction is created for each entry in the given {@code params} map, with the key being the suffix of
     * the generated test function name and the value being the parameter passed into the given bi-consumer. <strong>This method
     * is expected to be run from a class registered via the GameTestHolder annotation, rather than through the
     * RegisterGameTestsEvent event. It will behave unpredictably otherwise.</strong>
     * 
     * @param <T> the type of parameter to be accepted by the given test bi-consumer
     * @param generatedGroupName the method name of the calling test generator; used to compose the generated test name
     * @param options any options to be applied to the generated test; see the GameTest annotation for details
     * @param params a map of test name suffixes to test parameter values
     * @param consumer the bi-consumer to be transformed into a test function collection
     * @return the final collection of TestFunction objects, to be run in the Minecraft game test framework
     */
    public static <T> Collection<TestFunction> createParameterizedTestFunctions(String generatedGroupName, TestOptions options, Map<String, T> params, BiConsumer<GameTestHelper, T> consumer) {
        return createParameterizedTestFunctionsInner(findPrefix(2), generatedGroupName, options, params, consumer);
    }
    
    private static <T> Collection<TestFunction> createParameterizedTestFunctionsInner(String prefix, String generatedGroupName, TestOptions options, Map<String, T> params, BiConsumer<GameTestHelper, T> consumer) {
        List<TestFunction> retVal = new ArrayList<>();
        params.forEach((name, param) -> {
            retVal.add(createTestFunctionInner(prefix, generatedGroupName, name, options, helper -> {
                consumer.accept(helper, param);
            }));
        });
        return retVal;
    }
    
    public static void placeBed(GameTestHelper helper, BlockPos bedPos) {
        helper.setBlock(bedPos, Blocks.BLUE_BED);
        BlockState footState = helper.getBlockState(bedPos);
        BlockPos headPos = bedPos.relative(footState.getValue(BedBlock.FACING));
        helper.setBlock(headPos, footState.setValue(BedBlock.PART, BedPart.HEAD));
    }
}
