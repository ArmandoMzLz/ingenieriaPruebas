using System.Runtime.InteropServices.ObjectiveC;

namespace Calculadora
{
    public partial class Form1 : Form
    {
        float resultadoOperacion = 0;
        string simboloOperacion = string.Empty;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void clickBoton(object sender, EventArgs eventArgs)
        {
            if(textBox1.Text == "0")
                textBox1.Clear();

            Button button = (Button)sender;

            if (button.Text == ".")
            {
                if (!textBox1.Text.Contains("."))
                    textBox1.Text = textBox1.Text + button.Text;
            }
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
            switch(simboloOperacion)
            {
                case "+":
                    textBox1.Text = (resultadoOperacion + float.Parse(textBox1.Text)).ToString(); break;
                case "-":
                    textBox1.Text = (resultadoOperacion - float.Parse(textBox1.Text)).ToString(); break;
                case "x":
                    textBox1.Text = (resultadoOperacion * float.Parse(textBox1.Text)).ToString(); break;
                case "/":
                    textBox1.Text = (resultadoOperacion / float.Parse(textBox1.Text)).ToString(); break;
            }
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
    }
}
