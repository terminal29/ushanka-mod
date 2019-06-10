package com.terminal29.ushanka.extension;

import net.minecraft.structure.Structure;
import net.minecraft.util.Identifier;

public interface IStructureManagerExtension {
    Structure loadStructureFromFile(Identifier identifier_1);
}
