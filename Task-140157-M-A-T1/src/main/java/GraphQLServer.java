import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GraphQLServer {

    public static void main(String[] args) {
        // Parse the schema
        File schemaFile = new File("src/main/resources/schema.graphqls");
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        GraphQL graphQL = GraphQL.newGraphQL(schema).build();

        // Initialize DataLoaderRegistry
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        initializeDataLoaders(dataLoaderRegistry); // Call the initialization method

        // Sample GraphQL query to get a user
        String query = """
    query getUser($userId: ID!) {
        user(id: $userId) {
            id
            name
            posts {
                id
                title
                comments {
                    id
                    text
                }
            }
        }
    }
""";


        Map<String, Object> variables = Map.of("userId", 1L); // Sample variable
        ExecutionResult executionResult = graphQL.execute(query, String.valueOf(variables), dataLoaderRegistry);
        System.out.println(Optional.ofNullable(executionResult.getData())); // Print the result
    }

    // Method to initialize DataLoaders
    private static void initializeDataLoaders(DataLoaderRegistry dataLoaderRegistry) {
        DataLoader<Long, User> userLoader = new DataLoader<>(keys -> {
            List<User> users = keys.stream().map(GraphQLServer::fetchUser).collect(Collectors.toList());
            return CompletableFuture.completedFuture(users);
        });

        // Register the DataLoader
        dataLoaderRegistry.register("userLoader", userLoader);
    }

    // Simulate fetching a user from the database
    private static User fetchUser(Long userId) {
        // For now, return a mock User object with no posts (null) and a dynamic name
        return new User(userId, "User " + userId, null);
    }

    // Define how the GraphQL wiring should be built (with data fetchers)
    private static RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("user", dataFetchingEnvironment -> {
                            DataLoader<Long, User> userLoader = dataFetchingEnvironment.getDataLoader("userLoader");
                            long userId = dataFetchingEnvironment.getArgument("id");
                            return userLoader.load(userId);
                        })
                )
                .build();
    }
}
