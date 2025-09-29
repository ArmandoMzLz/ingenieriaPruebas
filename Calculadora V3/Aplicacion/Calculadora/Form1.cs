using System.Runtime.InteropServices.ObjectiveC;
using System.Runtime.Intrinsics.X86;

namespace Calculadora
{
    public partial class Form1 : Form
    {
        private float resultadoOperacion = 0;
        private float numeroMemoria = 0;
        private float numeroActual = 0;

        private int contador = 0;
        private const int operacionesMaximas = 5;

        private string simboloOperacion = string.Empty;
        private string[] historial = new string[operacionesMaximas];

        private bool operacionRealizada = false;
        private bool operacionDisponible = false;
        private bool operacionNueva = true;
        private bool error = false;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e) => panel1.Visible = false;

        private void clickBoton(object sender, EventArgs eventArgs)
        {
            Button button = (Button)sender;

            if(operacionRealizada)
            {
                textBox1.Clear();
                operacionRealizada = false;
            }

            if(textBox1.Text == "0" && button.Text != ".")
            {
                textBox1.Text = button.Text;
                return;
            }

            if(button.Text == "•")
            {
                if(!textBox1.Text.Contains("."))
                    textBox1.Text += ".";
                return;
            }

            int indicePuntoDecimal = textBox1.Text.IndexOf(".");
            if(indicePuntoDecimal >= 0)
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
            string operador = button.Text;

            if(operacionNueva)
            {
                resultadoOperacion = checarPorcentaje();
                operacionNueva = false;
            } else
            {
                aplicarOperacion();
            }
            
            simboloOperacion = operador;
            textBox1.Text = "0";
        }

        private void resultado(object sender, EventArgs eventArgs)
        {
            aplicarOperacion();
            operacionNueva = true;

            if (!operacionDisponible)
                label1.Text = String.Empty;
        }

        private void aplicarOperacion()
        {
            float numeroAnterior = resultadoOperacion;
            float numeroAct = checarPorcentaje();

            switch(simboloOperacion)
            {
                case "+": 
                    resultadoOperacion += checarPorcentaje();
                    break;
                case "-": resultadoOperacion -= checarPorcentaje();
                    break;
                case "x": resultadoOperacion *= checarPorcentaje();
                    break;
                case "÷":
                    if (checarPorcentaje() == 0)
                        error = true;
                    else
                        resultadoOperacion /= checarPorcentaje();
                break;
            }

            if(error)
            {
                textBox1.Text = "Math ERROR";
                resultadoOperacion = 0;
            }
            else
            {
                if(resultadoOperacion.ToString().Contains("."))
                    textBox1.Text = resultadoOperacion.ToString("0.00");
                else
                    textBox1.Text = resultadoOperacion.ToString("0");

                historialFuncion(numeroAnterior, simboloOperacion, numeroAct, resultadoOperacion);
            }

            operacionRealizada = true;
            operacionDisponible = true;
            error = false;
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

        private void historialFuncion(float numeroAnterior, string operador, float numeroAct, float resultado)
        {
            string result = string.Empty;

            if (resultado.ToString().Contains("."))
                result = resultado.ToString("0.00");
            else 
                result = resultado.ToString("0");

            historial[contador] = $"{numeroAnterior} {operador} {numeroAct} = {result}";

            contador = (contador + 1) % operacionesMaximas;

            label1.Text = "";
            for(int i = 0; i < operacionesMaximas; i++)
            {
                if (historial[i] != null)
                    label1.Text += historial[i] + "\n\n";
            }
        }

        private void memoriaRecuperar(object sender, EventArgs eventArgs) => textBox1.Text = numeroMemoria.ToString();

        private void memoriaClear(object sender, EventArgs eventArgs) => numeroMemoria = 0;

        private void memoriaSumar(object sender, EventArgs eventArgs) => numeroMemoria += float.Parse(textBox1.Text);

        private void memoriaRestar(object sender, EventArgs eventArgs) => numeroMemoria -= float.Parse(textBox1.Text);

        private void porcentaje(object sender, EventArgs eventArgs) => textBox1.Text += "%";

        private float checarPorcentaje()
        {
            string numeroPorcentaje = textBox1.Text;

            if (numeroPorcentaje.Contains("%"))
            {
                numeroPorcentaje = numeroPorcentaje.Replace("%", string.Empty);
                numeroActual = float.Parse(numeroPorcentaje) / 100;
            }
            else 
                numeroActual = float.Parse(numeroPorcentaje);

            return numeroActual;
        }
    }
}
