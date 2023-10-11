public class ComplexNumber {
    private float re, im;

    public ComplexNumber(){
        re = 0;
        im = 0;
    }
    public ComplexNumber(float real, float imag){
        re = real;
        im = imag;
    }

    public ComplexNumber add(ComplexNumber c){
        this.re += c.re;
        this.im += c.im;

        return this;
    }

    public ComplexNumber subtract(ComplexNumber c){
        this.re -= c.re;
        this.im -= c.im;

        return this;
    }

    public ComplexNumber multiply(ComplexNumber c){
        // (a + b*i)*(c + d*i) = ac + ad*i + bc*i + bd*i^2 = ac - bd + (ad + bc)*i
        float nouRe = this.re * c.re - this.im * c.im;
        float nouIm = this.re * c.im + this.im * c.re;

        this.re = nouRe;
        this.im = nouIm;

        return this;
    }

    public ComplexNumber divide(ComplexNumber c){
        // (a + b*i)/(c + d*i) = (ac + bd)/(c^2 + d^2) + (bc - ad)/(c^2 + d^2)*i
        float nouRe = (this.re * c.re + this.im * c.im) / (c.re * c.re + c.im * c.im);
        float nouIm = (this.im * c.re - this.re * c.im) / (c.re * c.re + c.im * c.im);

        this.re = nouRe;
        this.im = nouIm;

        return this;
    }

    public ComplexNumber conjugate(){
        this.im *= -1;

        return this;
    }

    @Override
    public String toString(){
        return re + " + " + im + "i";
    }
}
