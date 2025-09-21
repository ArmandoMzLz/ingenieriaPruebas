using System.Runtime.InteropServices.ObjectiveC;
using System.Runtime.Intrinsics.X86;

namespace Calculadora
{
    public partial class Form1 : Form
    {
        private float resultadoOperacion = 0;
        private int contador = 0;
        private float numeroMemoria = 0;

        private string simboloOperacion = string.Empty;
        private string[] historial = new string[5];

        private bool operacionRealizada = false;
        private bool operacionDisponible = false;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e) => panel1.Visible = false;

        private void clickBoton(object sender, EventArgs eventArgs)
        {
            Button button = (Button)sender;

            if (operacionRealizada)
            {
                textBox1.Clear();
                operacionRealizada = false;
            }

            if(textBox1.Text == "0" && button.Text != ".")
            {
                textBox1.Text = button.Text;
                return;
            }

            if(button.Text == ".")
            {
                if(!textBox1.Text.Contains("."))
                    textBox1.Text += ".";
                return;
            }

            int indicePuntoDecimal = textBox1.Text.IndexOf(".");
            if (indicePuntoDecimal >= 0)
            {
                string decimales = textBox1.Text.Substring(indicePuntoDecimal + 1);

                if (decimales.Length >= 2) 
                    return;
            }

            textBox1.Text += button.Text; 
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
                    if (float.Parse(textBox1.Text) == 0)
                    {
                        textBox1.Text = "Math ERROR"; break;
                    }
                    else
                    {
                        textBox1.Text = (resultadoOperacion / float.Parse(textBox1.Text)).ToString("0.00"); break;
                    }
            }

            if (!operacionDisponible)
                label1.Text = String.Empty;

            if (textBox1.Text != "Math ERROR")
                historialFuncion(aux);

            operacionRealizada = true;
            operacionDisponible = true;
        }

        private void clearAll(object sender, EventArgs eventArgs)
        {
            textBox1.Text = "0";
            resultadoOperacion = 0;
        }

        private void clear(object sender, EventArgs eventArgs) => textBox1.Text = "0";

        private void historialOperaciones(object sender, EventArgs eventArgs)
        {
            if (!panel1.Visible)
                panel1.Visible = true;
            else
                panel1.Visible = false;
        }

        private void historialFuncion(string aux)
        {
            historial[contador] = resultadoOperacion + " " + simboloOperacion + " " + aux + " = " + textBox1.Text;

            contador = (contador + 1) % 5;

            label1.Text = "";
            for(int i = 0; i < 5; i++)
            {
                if (historial[i] != null)
                    label1.Text += historial[i] + "\n\n";
            }
        }

        private void memoriaRecuperar(object sender, EventArgs eventArgs) => numeroMemoria = float.Parse(textBox1.Text);

        private void memoriaClear(object sender, EventArgs eventArgs) => numeroMemoria = 0;

        private void memoriaSumar(object sender, EventArgs eventArgs) => textBox1.Text = (float.Parse(textBox1.Text) + numeroMemoria).ToString();

        private void memoriaRestar(object sender, EventArgs eventArgs) => textBox1.Text = (float.Parse(textBox1.Text) - numeroMemoria).ToString();
    }
}
