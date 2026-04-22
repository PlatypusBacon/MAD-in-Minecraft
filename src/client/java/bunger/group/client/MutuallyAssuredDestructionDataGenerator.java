package bunger.group.client;

import bunger.group.client.alex.datagen.AlexRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MutuallyAssuredDestructionDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack alexPack = fabricDataGenerator.createPack();
		alexPack.addProvider(AlexRecipeProvider::new);
	}
}
