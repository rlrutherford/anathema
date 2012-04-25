package net.sf.anathema.character.generic.impl.magic.persistence;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import net.sf.anathema.character.generic.impl.magic.SpellException;
import net.sf.anathema.character.generic.impl.magic.ThaumaturgyDegree;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.ThaumaturgyRank;
import net.sf.anathema.initialization.ExtensibleDataSetCompiler;
import net.sf.anathema.initialization.IExtensibleDataSetCompiler;
import net.sf.anathema.lib.resources.IAnathemaResourceFile;
import net.sf.anathema.lib.resources.IExtensibleDataSet;
import net.sf.anathema.lib.util.IIdentificate;

@ExtensibleDataSetCompiler
public class ThaumaturgyCompiler implements IExtensibleDataSetCompiler{
	private static final String Thaumaturgy_File_Recognition_Pattern = "Thaumaturgy.*\\.xml";
	private final List<Document> thaumaturgyFileList = new ArrayList<Document>();
	private final SAXReader reader = new SAXReader();
	private final ThaumaturgyCache cache = new ThaumaturgyCache();
	private final ThaumaturgyBuilder builder = new ThaumaturgyBuilder();
	
	@Override
	public String getName() {
		return "Thaumaturgy";
	}

	@Override
	public String getRecognitionPattern() {
		return Thaumaturgy_File_Recognition_Pattern;
	}

	@Override
	public void registerFile(IAnathemaResourceFile resource) throws Exception {
		try {
		      thaumaturgyFileList.add(reader.read(resource.getURL()));
		} catch (DocumentException e) {
		      throw new SpellException(resource.getURL().toExternalForm(), e);
		}
	}

	@Override
	public IExtensibleDataSet build() {
		for (Document document : thaumaturgyFileList) {
			IThaumaturgy[] rituals = builder.buildRituals(document);
			for (IThaumaturgy procedures : rituals) {
				cache.addThaumaturgy(procedures);
			}
		}
		for (IIdentificate art : cache.getArts()) {
			for (ThaumaturgyRank rank : ThaumaturgyRank.values()) {
				if (rank == ThaumaturgyRank.Initiate) {
					continue;
				}
				cache.addThaumaturgy(new ThaumaturgyDegree(art, rank));
			}
		}
		
		return cache;
	}

}
