package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.apache.commons.lang3.function.TriConsumer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TestUtils {
    public static final String DEFAULT_BATCH = "defaultBatch";

    @Nullable
    private static String findPrefix(int stackDepth) {
        String callerClassName = StackWalker.getInstance().walk(s -> s.skip(stackDepth).findFirst()).map(f -> f.getClassName()).orElse(null);
        if (callerClassName != null) {
            try {
                Class<?> callerClass = Class.forName(callerClassName);
                return Services.TEST.getTestNamespace(callerClass);
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
     * @param templateName the name of the structure template to use for the generated test
     * @param consumer the consumer to be transformed into a test function
     * @return the final TestFunction to be run in the Minecraft game test framework
     */
    public static TestFunction createTestFunction(String generatedGroupName, String nameSuffix, String templateName, Consumer<GameTestHelper> consumer) {
        return createTestFunctionInner(findPrefix(2), generatedGroupName, nameSuffix, TestOptions.builder(templateName).build(), consumer);
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
     * @param templateName the name of the structure template to use for the generated test
     * @param params a map of test name suffixes to test parameter values
     * @param consumer the bi-consumer to be transformed into a test function collection
     * @return the final collection of TestFunction objects, to be run in the Minecraft game test framework
     */
    public static <T> Collection<TestFunction> createParameterizedTestFunctions(String generatedGroupName, String templateName, Map<String, T> params, BiConsumer<GameTestHelper, T> consumer) {
        return createParameterizedTestFunctionsInner(findPrefix(2), generatedGroupName, TestOptions.builder(templateName).build(), params, consumer);
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

    /**
     * Transforms the given tri-consumer into a collection of TestFunction objects that can be run via the Minecraft game test
     * framework with default options. One TestFunction is created for each entry combination in the given {@code params1} and
     * {@code params2} map2, with the keys being the joined suffix of the generated test function name and the values being the
     * parameters passed into the given tri-consumer. <strong>This method is expected to be run from a class registered via the
     * GameTestHolder annotation, rather than through the RegisterGameTestsEvent event. It will behave unpredictably otherwise.</strong>
     *
     * @param <T> the first type of parameter to be accepted by the given test tri-consumer
     * @param <U> the second type of parameter to be accepted by the given test tri-consumer
     * @param generatedGroupName the method name of the calling test generator; used to compose the generated test name
     * @param templateName the name of the structure template to use for the generated test
     * @param params1 a map of test name suffixes to test parameter values for the tri-consumer's first parameter
     * @param params2 a map of test name suffixes to test parameter values for the tri-consumer's second parameter
     * @param consumer the tri-consumer to be transformed into a test function collection
     * @return the final collection of TestFunction objects, to be run in the Minecraft game test framework
     */
    public static <T, U> Collection<TestFunction> createDualParameterizedTestFunctions(String generatedGroupName,
                                                                                       String templateName,
                                                                                       Map<String, T> params1,
                                                                                       Map<String, U> params2,
                                                                                       TriConsumer<GameTestHelper, T, U> consumer) {
        return createDualParameterizedTestFunctionsInner(findPrefix(2), generatedGroupName, TestOptions.builder(templateName).build(), params1, params2, consumer);
    }

    private static <T, U> Collection<TestFunction> createDualParameterizedTestFunctionsInner(String prefix,
                                                                                             String generatedGroupName,
                                                                                             TestOptions options,
                                                                                             Map<String, T> params1,
                                                                                             Map<String, U> params2,
                                                                                             TriConsumer<GameTestHelper, T, U> consumer) {
        List<TestFunction> retVal = new ArrayList<>();
        params1.forEach((name1, param1) -> {
            params2.forEach((name2, param2) -> {
                retVal.add(createTestFunctionInner(prefix, generatedGroupName, String.join(".", name1, name2), options, helper -> {
                    consumer.accept(helper, param1, param2);
                }));
            });
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
