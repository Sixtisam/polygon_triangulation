package net.skeeks.efalg.poly_tria;


class Outer
{
    public void method1()
    {
        int x = 10;
        class Inner
        {
            public void inMethod()
            {
                System.out.println(x);
            }
        }
        Inner i  = new Inner();
        i.inMethod();
    }
    
    public static void main(String[] args)
    {
        Outer obj = new Outer();
        obj.method1();
    } 
}