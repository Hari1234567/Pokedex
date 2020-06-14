package com.example.pokedex;

import android.widget.GridLayout;

import java.util.ArrayList;

public class IndividualData {
    private Sprites sprites;
    private int height = 0;
    private int weight = 0;
    private int base_experience = 0;
    private ArrayList<Stats> stats;
    private ArrayList<Ability> abilities;
    private ArrayList<Moves> moves;
    private Species species;
    private String name;

    public String getName() {
        return name;
    }

    private int id;

    public int getId() {
        return id;
    }

    public Species getSpecies() {
        return species;
    }

    public ArrayList<Moves> getMoves() {
        return moves;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getBase_experience() {
        return base_experience;
    }


    public Sprites getSprites() {
        return sprites;
    }


    public ArrayList<Stats> getStats() {
        return stats;
    }

    class Stats {
        private int base_stat = 0;

        public int getBase_stat() {
            return base_stat;
        }

        public Stat getStat() {
            return stat;
        }

        private Stat stat;

        class Stat {
            String name;

            public String getName() {
                return name;
            }
        }
    }

    class Ability {


        private Able ability;

        public Able getAbility() {
            return ability;
        }

        class Able {
            private String name;

            public String getName() {
                return name;
            }
        }

    }

    class Moves {
        public Move getMove() {
            return move;
        }

        private Move move;

        class Move {
            private String name;

            public String getName() {
                return name;
            }
        }
    }

    class Species {
        private String name;


        public String getName() {
            return name;
        }

        class SpeciesData {
             private EvolvesFrom evolves_from_species;
            private ArrayList<Variety> varieties;

            public ArrayList<Variety> getVarieties() {
                return varieties;
            }

            public EvolvesFrom getEvolves_from_species() {
                return evolves_from_species;
            }

            class EvolvesFrom{
                   private String name;

                   public String getSpeciesName() {
                       return name;
                   }
               }


        }

        class Variety{
            private PokeVariety pokemon;

            public PokeVariety getPokemon() {
                return pokemon;
            }

            class PokeVariety{
                private String name;

                public String getName() {
                    return name;
                }
            }
        }
    }
}

