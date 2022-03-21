/**
 * MultiBlock System<p>
 *
 * Used to make placing & breaking multi-space holding blocks easier
 * <br>
 * Recommended to make use Registrator & the following custom BlockBuilder
 * <ul>
 * <li> {@link xyz.apex.forge.apexcore.lib.ApexRegistrator#multiBlock(java.lang.String, java.lang.Object, xyz.apex.forge.apexcore.lib.multiblock.MultiBlockFactory, xyz.apex.forge.apexcore.lib.multiblock.MultiBlockPattern)}
 * <li> {@link xyz.apex.forge.apexcore.lib.multiblock.MultiBlockBuilder}
 * </ul>
 * Although the default Registrator BlockBuilder would work fine
 * <br><br>
 * Original System Developed by: 50ap5ud5#8701<br>
 * Source Code: https://gitlab.com/Spectre0987/TardisMod-1-14/-/tree/1.16
 */
@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault @FieldsAreNonnullByDefault
package xyz.apex.forge.apexcore.lib.multiblock;

import xyz.apex.java.utility.nullness.FieldsAreNonnullByDefault;
import xyz.apex.java.utility.nullness.MethodsReturnNonnullByDefault;
import xyz.apex.java.utility.nullness.ParametersAreNonnullByDefault;