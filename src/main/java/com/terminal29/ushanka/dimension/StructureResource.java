package com.terminal29.ushanka.dimension;

import net.minecraft.resource.ResourceImpl;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StructureResource extends ResourceImpl {

    private byte[] data;

    public StructureResource(String string_1, Identifier identifier_1, InputStream inputStream_1, InputStream inputStream_2) {
        super(string_1, identifier_1, inputStream_1, inputStream_2);
        try {
            data = IOUtils.toByteArray(super.getInputStream());
        }catch(IOException ignored){

        }
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }
}
