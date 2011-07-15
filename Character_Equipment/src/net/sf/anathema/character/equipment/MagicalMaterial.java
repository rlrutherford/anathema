package net.sf.anathema.character.equipment;

import net.sf.anathema.character.alchemical.caste.AlchemicalCaste;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.equipment.ArtifactAttuneType;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.generic.type.ICharacterTypeVisitor;
import net.sf.anathema.lib.util.IIdentificate;

public enum MagicalMaterial implements IIdentificate {
  Orichalcum, Jade, Moonsilver, Starmetal, Soulsteel, Adamant;

  public String getId() {
    return name();
  }
  
  public static ArtifactAttuneType[] getAttunementTypes(final ICharacterType type,
		  final ICasteType caste,
		  final MagicalMaterial material)
  {
	  final ArtifactAttuneType[][] types = new ArtifactAttuneType[1][];
	  
	  type.accept(new ICharacterTypeVisitor()
	  {
		@Override
		public void visitAbyssal(ICharacterType visitedType)
		{
			types[0] = getSingleMaterialAttunement(type, caste, material);
		}

		@Override
		public void visitDB(ICharacterType visitedType) {
			types[0] = getSingleMaterialAttunement(type, caste, material);
		}

		@Override
		public void visitDragonKing(ICharacterType type) {
			types[0] = null;
		}

		@Override
		public void visitLunar(ICharacterType type) {
			types[0] = getSingleMaterialAttunement(type, caste, material);
		}
		
		@Override
		public void visitInfernal(ICharacterType type) {
			types[0] = new ArtifactAttuneType[] { ArtifactAttuneType.Unattuned, 
					ArtifactAttuneType.PartiallyAttuned, ArtifactAttuneType.VitriolAttuned,
					ArtifactAttuneType.FullyAttuned };
		}

		@Override
		public void visitMortal(ICharacterType visitedType) {
			types[0] = null;
		}

		@Override
		public void visitSidereal(ICharacterType visitedType) {
			types[0] = getSingleMaterialAttunement(type, caste, material);
		}

		@Override
		public void visitSolar(ICharacterType visitedType) {
			types[0] = getSingleMaterialAttunement(type, caste,  material);
		}

		@Override
		public void visitGhost(ICharacterType type) {
			if (material == MagicalMaterial.Orichalcum ||
				material == MagicalMaterial.Moonsilver ||
				material == MagicalMaterial.Starmetal)
				types[0] = new ArtifactAttuneType[]
				    { ArtifactAttuneType.Unattuned, ArtifactAttuneType.ExpensivePartiallyAttuned };
			else
				types[0] = new ArtifactAttuneType[]
				    { ArtifactAttuneType.Unattuned, ArtifactAttuneType.PartiallyAttuned };
		}

		@Override
		public void visitSpirit(ICharacterType type) {
			types[0] = null;
		}

		@Override
		public void visitAlchemical(ICharacterType visitedType) {
			types[0] = getSingleMaterialAttunement(type, caste,  material);
		}
	  });
	  
	  return types[0];
  }
  
  private static ArtifactAttuneType[] getSingleMaterialAttunement(ICharacterType type, ICasteType caste, MagicalMaterial material)
  {
	  ArtifactAttuneType[] attunement;
	  if (material == getDefault(type, caste))
		  attunement = new ArtifactAttuneType[] { ArtifactAttuneType.Unattuned, ArtifactAttuneType.FullyAttuned };
	  else
		  attunement = new ArtifactAttuneType[] { ArtifactAttuneType.Unattuned,
			  ArtifactAttuneType.PartiallyAttuned, ArtifactAttuneType.UnharmoniouslyAttuned };
	  return attunement;
  }

  public static MagicalMaterial getDefault(ICharacterType characterType, final ICasteType caste) {
    final MagicalMaterial[] material = new MagicalMaterial[1];

    characterType.accept(new ICharacterTypeVisitor() {

      public void visitSolar(ICharacterType visitedType) {
        material[0] = Orichalcum;
      }

      public void visitSidereal(ICharacterType visitedType) {
        material[0] = Starmetal;
      }
      
      public void visitAlchemical(ICharacterType visitedType) {
          if (caste == AlchemicalCaste.Orichalcum)
        	  material[0] = Orichalcum;
          if (caste == AlchemicalCaste.Moonsilver)
        	  material[0] = Moonsilver;
          if (caste == AlchemicalCaste.Starmetal)
        	  material[0] = Starmetal;
          if (caste == AlchemicalCaste.Jade)
        	  material[0] = Jade;
          if (caste == AlchemicalCaste.Soulsteel)
        	  material[0] = Soulsteel;
          if (caste == AlchemicalCaste.Adamant)
        	  material[0] = Adamant;
        }

      public void visitMortal(ICharacterType visitedType) {
        // nothing to do
      }
      
      public void visitSpirit(ICharacterType visitedType) {
          // nothing to do
        }
      
      public void visitGhost(ICharacterType visitedType)
      {
    	  // nothing to do
      }

      public void visitLunar(ICharacterType type) {
        material[0] = Moonsilver;
      }

      public void visitDragonKing(ICharacterType type) {
        material[0] = Orichalcum;
      }

      public void visitDB(ICharacterType visitedType) {
        material[0] = Jade;
      }
      
      public void visitInfernal(ICharacterType visitedType)
      {
    	material[0] = Orichalcum;
      }

      public void visitAbyssal(ICharacterType visitedType) {
        material[0] = Soulsteel;
      }
    });
    return material[0];
  }
}