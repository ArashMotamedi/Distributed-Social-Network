using System;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System.IO;

public partial class images_TextImage : System.Web.UI.Page
{
	protected void Page_Load(object sender, EventArgs e)
	{
		// Prepare bitmap and graphics objects (tentative size at this point)
		Bitmap b = new Bitmap(1000, 100);
		Graphics g = Graphics.FromImage(b);

		// Set graphics options
		g.SmoothingMode = SmoothingMode.AntiAlias;
		g.TextRenderingHint = System.Drawing.Text.TextRenderingHint.AntiAlias;

		// Prepare the font and the brush and the text
		TextStyle tc = new TextStyle(Request.QueryString["style"]);
		string text = Request.QueryString["text"];

		// Write the text
		g.DrawString(text, tc.font, tc.brush, 0, 0 - tc.topCut);

		// Crop the bitmap!
		SizeF sf = g.MeasureString(text, tc.font);
		int measuredWidth = (int)sf.Width;
		int measuredHeight = (int)sf.Height - tc.topCut - tc.bottomCut;
		int offsetLeft = 0;

		bool stop = false;
		Color transparent = Color.FromArgb(0, 0, 0, 0);

		// sf is inaccurate, trim the right side of the thing!
		for (int x = measuredWidth - 1; x > 0; x--)
		{
			for (int y = 0; y < measuredHeight; y++)
			{
				if (b.GetPixel(x, y) != transparent)
				{
					// We hit something! Quit!
					measuredWidth = x + 1;
					stop = true;
					break;
				}
			}
			if (stop) break;
		}

		// Trim the left!
		stop = false;
		for (int x = 1; x < measuredWidth; x++)
		{
			for (int y = 0; y < measuredHeight; y++)
			{
				if (b.GetPixel(x, y) != transparent)
				{
					// We hit something! Quit!
					offsetLeft = x - 1;
					stop = true;
					break;
				}
			}
			if (stop) break;
		}

		Bitmap bo = b.Clone(new RectangleF(new PointF(offsetLeft, 0), new SizeF(measuredWidth - offsetLeft, measuredHeight)), b.PixelFormat);

		// Dispose of the old bitmap and graphics
		b.Dispose();
		g.Dispose();

		// Save from bitmap to a stream writeable to the output stream
		MemoryStream ms = new MemoryStream();
		bo.Save(ms, ImageFormat.Png);

		// Dispose of bitmap
		bo.Dispose();

		// Write the image on the output
		Response.ContentType = "image/png";
		ms.WriteTo(Response.OutputStream);

		// Dispose of stream
		ms.Dispose();
	}

	private class TextStyle
	{
		public Font font;
		public Brush brush;
		public int topCut = 0;
		public int bottomCut = 0;

		public TextStyle(string style)
		{
			Color color = Color.FromArgb(52, 62, 64);
			string fontName = "Pupcat";
			//fontName = "Lindsey";
			//fontName = "SketchFlow Print";
			//fontName = "Tempus Sans ITC";

			int fontSize = 20;
			FontStyle fontStyle = FontStyle.Regular;

			// Set the font
			switch (style)
			{
				case "h1":
					color = Color.FromArgb(52, 62, 64);
					fontName = "SketchFlow Print";
					fontStyle = FontStyle.Bold;
					break;
				case "h2":
					fontName = "SketchFlow Print";
					color = Color.FromArgb(37, 94, 52);
					fontStyle = FontStyle.Bold;
					fontSize = 16;
					topCut = 0;
					bottomCut = 0;
					break;

				case "message-error":
					fontName = "SketchFlow Print";
					color = Color.FromArgb(145, 56, 56);
					fontStyle = FontStyle.Bold;
					fontSize = 13;
					topCut = 0;
					bottomCut = 0;
					break;
				case "message-success":
					fontName = "SketchFlow Print";
					color = Color.FromArgb(37, 94, 52);
					fontStyle = FontStyle.Bold;
					fontSize = 13;
					topCut = 0;
					bottomCut = 0;
					break;
				case "button":
					color = Color.FromArgb(20, 20, 20);
					fontName = "Pupcat";
					fontStyle = FontStyle.Regular;
					fontSize = 15;
					topCut = 4;
					bottomCut = 4;
					break;
				case "MenuTab":
					color = Color.White;
					fontName = "B Tabassom";
					fontStyle = FontStyle.Regular;
					fontSize = 27;
					topCut = 10;
					bottomCut = 10;
					break;
				default:
					break;
			}

			// Set the font and the brush
			font = new Font(new Font(fontName, fontSize, GraphicsUnit.Point), fontStyle);
			brush = new SolidBrush(color);
		}
	}
}
