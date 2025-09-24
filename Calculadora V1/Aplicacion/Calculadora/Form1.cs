using System.Runtime.InteropServices.ObjectiveC;
using System.Runtime.Intrinsics.X86;

namespace Calculadora
{
    public partial class Form1 : Form
    {
        private float resultadoOperacion = 0;
        private int contador = 0;

        private string simboloOperacion = string.Empty;
        private string[] historial = new string[5];

        private bool operacionRealizada = false;
        private bool operacionDisponible = false;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            panel1.Visible = false;
        }

        private void clickBoton(object sender, EventArgs eventArgs)
        {
            Button button = (Button)sender;

            int indicePuntoDecimal = textBox1.Text.IndexOf(".");
            bool masDeDosDecimales = indicePuntoDecimal >= 0 && textBox1.Text.Length - indicePuntoDecimal - 1 > 1;

            if (textBox1.Text == "0")
                textBox1.Clear();

            if (operacionRealizada)
            {
                textBox1.Clear();
                operacionRealizada = false;
            }

            if (button.Text == ".")
            {
                if (!textBox1.Text.Contains("."))
                    textBox1.Text = textBox1.Text + button.Text;
            }
            else if (masDeDosDecimales)
                textBox1.Text = textBox1.Text;
            else
                textBox1.Text = textBox1.Text + button.Text;
        }

        private void operacion(object sender, EventArgs eventArgs)
        {
            Button button = (Button)sender;
            simboloOperacion = button.Text;
            resultadoOperacion = float.Parse(textBox1.Text);

            textBox1.Text = "0";
        }

        private void resultado(object sender, EventArgs eventArgs)
        {
            string aux = textBox1.Text;

            switch (simboloOperacion)
            {
                case "+":
                    textBox1.Text = (resultadoOperacion + float.Parse(textBox1.Text)).ToString("0.00"); break;
                case "-":
                    textBox1.Text = (resultadoOperacion - float.Parse(textBox1.Text)).ToString("0.00"); break;
                case "x":
                    textBox1.Text = (resultadoOperacion * float.Parse(textBox1.Text)).ToString("0.00"); break;
                case "/":
                    if(float.Parse(textBox1.Text) == 0)
                    {
                        textBox1.Text = "Math ERROR"; break;
                    } else
                    {
                        textBox1.Text = (resultadoOperacion / float.Parse(textBox1.Text)).ToString("0.00"); break;
                    }
            }

            if (!operacionDisponible)
                label1.Text = String.Empty;

            if(textBox1.Text != "Math ERROR")
                historialFuncion(aux);

            operacionRealizada = true;
            operacionDisponible = true;
        }

        private void clearAll(object sender, EventArgs eventArgs)
        {
            textBox1.Text = "0";
            resultadoOperacion = 0;
        }

        private void clear(object sender, EventArgs eventArgs)
        {
            textBox1.Text = "0";
        }

        private void historialOperaciones(object sender, EventArgs eventArgs)
        {
            if (!panel1.Visible)
                panel1.Visible = true;
            else
                panel1.Visible = false;
        }

        private void historialFuncion(string aux)
        {
            if(contador == 4)
                contador = 0;

            historial[contador] = resultadoOperacion + " " + simboloOperacion + " " + aux + " = " + textBox1.Text;
            label1.Text = label1.Text + historial[contador] + "\n\n";

            contador++;
        }
    }
}
