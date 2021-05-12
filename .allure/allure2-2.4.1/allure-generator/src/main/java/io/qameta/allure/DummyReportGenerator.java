package io.qameta.allure;

import io.qameta.allure.allure1.Allure1Plugin;
import io.qameta.allure.allure2.Allure2Plugin;
import io.qameta.allure.category.CategoriesPlugin;
import io.qameta.allure.context.FreemarkerContext;
import io.qameta.allure.context.JacksonContext;
import io.qameta.allure.context.MarkdownContext;
import io.qameta.allure.context.RandomUidContext;
import io.qameta.allure.core.AttachmentsPlugin;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.core.MarkdownDescriptionsPlugin;
import io.qameta.allure.core.Plugin;
import io.qameta.allure.core.ReportWebPlugin;
import io.qameta.allure.core.TestsResultsPlugin;
import io.qameta.allure.environment.Allure1EnvironmentPlugin;
import io.qameta.allure.executor.ExecutorPlugin;
import io.qameta.allure.graph.GraphPlugin;
import io.qameta.allure.history.HistoryPlugin;
import io.qameta.allure.history.HistoryTrendPlugin;
import io.qameta.allure.launch.LaunchPlugin;
import io.qameta.allure.mail.MailPlugin;
import io.qameta.allure.owner.OwnerPlugin;
import io.qameta.allure.plugin.DefaultPluginLoader;
import io.qameta.allure.retry.RetryPlugin;
import io.qameta.allure.severity.SeverityPlugin;
import io.qameta.allure.suites.SuitesPlugin;
import io.qameta.allure.summary.SummaryPlugin;
import io.qameta.allure.tags.TagsPlugin;
import io.qameta.allure.timeline.TimelinePlugin;
import io.qameta.allure.widget.WidgetsPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Artem Eroshenko eroshenkoam@qameta.io
 *         Date: 1/22/14
 */
@SuppressWarnings("PMD.ExcessiveImports")
public final class DummyReportGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyReportGenerator.class);
    private static final int MIN_ARGUMENTS_COUNT = 2;
    private static final List<Extension> EXTENSIONS = Arrays.asList(
            new JacksonContext(),
            new MarkdownContext(),
            new FreemarkerContext(),
            new RandomUidContext(),
            new MarkdownDescriptionsPlugin(),
            new RetryPlugin(),
            new TagsPlugin(),
            new SeverityPlugin(),
            new OwnerPlugin(),
            new CategoriesPlugin(),
            new HistoryPlugin(),
            new HistoryTrendPlugin(),
            new GraphPlugin(),
            new TimelinePlugin(),
            new SuitesPlugin(),
            new TestsResultsPlugin(),
            new AttachmentsPlugin(),
            new MailPlugin(),
            new WidgetsPlugin(),
            new SummaryPlugin(),
            new ExecutorPlugin(),
            new LaunchPlugin(),
            new Allure1Plugin(),
            new Allure1EnvironmentPlugin(),
            new Allure2Plugin(),
            new ReportWebPlugin() {
                @Override
                public void aggregate(final Configuration configuration,
                                      final List<LaunchResults> launchesResults,
                                      final Path outputDirectory) throws IOException {
                    writePluginsStatic(configuration, outputDirectory);
                    writeIndexHtml(configuration, outputDirectory);
                }
            }
    );

    private DummyReportGenerator() {
        throw new IllegalStateException("Do not instance");
    }

    /**
     * Generate Allure report data from directories with allure report results.
     *
     * @param args a list of directory paths. First (args.length - 1) arguments -
     *             results directories, last argument - the folder to generated data
     */
    public static void main(final String... args) throws IOException {
        if (args.length < MIN_ARGUMENTS_COUNT) {
            LOGGER.error("There must be at least two arguments");
            return;
        }
        int lastIndex = args.length - 1;
        final Path[] files = getFiles(args);
        final List<Plugin> plugins = loadPlugins();
        LOGGER.info("Found {} plugins", plugins.size());
        plugins.forEach(plugin -> LOGGER.info(plugin.getConfig().getName()));
        final Configuration configuration = new ConfigurationBuilder()
                .fromExtensions(EXTENSIONS)
                .fromPlugins(plugins)
                .build();
        final ReportGenerator generator = new ReportGenerator(configuration);
        generator.generate(files[lastIndex], Arrays.copyOf(files, lastIndex));
    }

    public static Path[] getFiles(final String... paths) {
        return Arrays.stream(paths)
                .map(Paths::get)
                .toArray(Path[]::new);
    }

    public static List<Plugin> loadPlugins() throws IOException {
        final Optional<Path> optional = Optional.ofNullable(System.getProperty("allure.plugins.directory"))
                .map(Paths::get)
                .filter(Files::isDirectory);
        if (!optional.isPresent()) {
            return Collections.emptyList();
        }
        final Path pluginsDirectory = optional.get();
        LOGGER.info("Found plugins directory {}", pluginsDirectory);
        final DefaultPluginLoader loader = new DefaultPluginLoader();
        final ClassLoader classLoader = DummyReportGenerator.class.getClassLoader();
        return Files.list(pluginsDirectory)
                .filter(Files::isDirectory)
                .map(pluginDir -> loader.loadPlugin(classLoader, pluginDir))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}