package goblinbob.mobends.standard.client.model.armor;

import goblinbob.mobends.core.client.model.MutatedBox;
import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.*;

@Deprecated
public class PartBoxes
{
    protected HashMap<ModelRenderer, List<MutatedBox>> modelToBoxesMap = new HashMap<>();

    public void put(ModelRenderer part, MutatedBox box)
    {
        if (!modelToBoxesMap.containsKey(part))
        {
            modelToBoxesMap.put(part, new LinkedList<>());
        }

        modelToBoxesMap.get(part).add(box);
    }

    public void clear()
    {
        this.modelToBoxesMap.clear();
    }

    public void clearPart(ModelRenderer part)
    {
        modelToBoxesMap.remove(part);
    }

    public Set<Map.Entry<ModelRenderer, List<MutatedBox>>> entrySet()
    {
        return modelToBoxesMap.entrySet();
    }

    public Set<ModelRenderer> keySet()
    {
        return modelToBoxesMap.keySet();
    }
}
