package codes.ztereohype.nicerskies.sky;

import codes.ztereohype.nicerskies.NicerSkies;
import codes.ztereohype.nicerskies.config.ConfigManager;
import codes.ztereohype.nicerskies.core.Gradient;
import codes.ztereohype.nicerskies.sky.nebula.NebulaSkyboxPainter;
import codes.ztereohype.nicerskies.sky.nebula.Skybox;
import codes.ztereohype.nicerskies.sky.star.Starbox;
import com.mojang.blaze3d.vertex.VertexBuffer;
import lombok.Getter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

import java.util.stream.IntStream;

public class SkyManager {
    private @Getter Starbox starbox;
    private @Getter Skybox skybox;
    private @Getter boolean isInitialized = false;

    private final Gradient starGradient = new Gradient();
    private final Gradient nebulaGradient = new Gradient();

    public void generateSky(long seed) {
        ConfigManager cm = NicerSkies.config;

        nebulaGradient.clear();
        starGradient.clear();

        buildGradients();

        RandomSource randomSource = RandomSource.create(seed);

        PerlinNoise perlinNoise = PerlinNoise.create(randomSource, IntStream.of(1, 2, 3, 4, 5));
        NebulaSkyboxPainter painter = new NebulaSkyboxPainter(perlinNoise, nebulaGradient, cm.getNebulaNoiseScale(), cm.getNebulaNoiseAmount(), cm.getNebulaBaseColourAmount());

        this.starbox = new Starbox(randomSource, starGradient);
        this.skybox = new Skybox(painter);
    }

    public void tick(int ticks, VertexBuffer starBuffer) {
        this.starbox.updateStars(ticks, starBuffer);
        this.isInitialized = true;
    }

    public void buildGradients() {
        starGradient.add(0.0f, 255, 179, 97);
        starGradient.add(0.2f, 255, 249, 253);
        starGradient.add(1.0f, 175, 199, 255);

        nebulaGradient.add(0.2f, 41, 98, 146);
        nebulaGradient.add(0.5f, 120, 59, 93);
        nebulaGradient.add(0.7f, 209, 72, 103);
        nebulaGradient.add(0.8f, 255, 200, 123);
        nebulaGradient.add(1.0f, 253, 243, 220);
    }
}
