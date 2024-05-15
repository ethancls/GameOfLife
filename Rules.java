public class Rules {
    
}
    
    /* Fonction recursive
    public getValue(Tree t) // bien mettre val 1 en noeud gauche et val 2 en noeud droit
    {
        if(leaf is int)
        {
            return leaf;
        }
        else if (leaf is operator)
        {
            if(operator is ET)
            {
                if(getValue(left) != 0 && getValue(right) != 0)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else if(operator is OU)
            {
               if(getValue(left) != 0 || getValue(right) != 0)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else if(operator is NON)
            {
                if(getValue(left) == 0) // Val1 node left
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else if(operator is SUP)
            {
                if(getValue(left) > getValue(right))
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else if(operator is SUPEQ)
            {
                if(getValue(left) >= getValue(right))
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else if(operator is EQ)
            {
                if(getValue(left) == getValue(right))
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else if(operator is COMPTER)
            {
                if(getValue(left) or getValue(right) is voisinage)
                {
                    return nb de cellules voisines a l'état 1;
                }
            }
            else if(operator is ADD)
            {
                return getValue(left) + getValue(right);
            }
            else if(operator is SUB)
            {
                return getValue(left) - getValue(right);
            }
            else if(operator is MUL)
            {
                return getValue(left) * getValue(right);
            }
            else if(operator is SI) // 3 enfants car 3 valeurs
            {
               if(getValue(left) != 0)
               {
                   return getValue(middle);
               }
               else
               {
                   return getValue(right);
               }
            }
        }
    }*/

