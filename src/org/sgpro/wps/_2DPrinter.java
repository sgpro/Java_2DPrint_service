package org.sgpro.wps;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
  
  
@Path("/_2Dprint")
public class _2DPrinter extends HttpServlet implements Printable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PrinterJob job;
	Graphics2D g ;

	private void drawInRect(Rectangle r, Font f, String text, int lineMargin) {
		FontMetrics fm = g.getFontMetrics(f);
		// int margin = 10; // 行距
		int w = r.width;
		int h = r.height;
		
		float fh = f.getSize2D();// 字体高度
		float textWidth = fm.stringWidth(text);
		
		int textLine = (int)(textWidth / w) + 1;
		float textHeight = textLine * (fh + lineMargin);
		
		String str = text;
		int cw = 0;
		List<Integer> l = new ArrayList<>();
				
		for (int i = 0; i < str.length(); i++ ) {
			char c = str.charAt(i);
			cw += fm.charWidth(c);
			if (cw > r.width) {
				l.add(--i);
				cw = 0;
			}
		}
			
		l.add(str.length());
		
		int from = 0;
		int to = 0;
		String line = null;
		
		float y = (h-textHeight)/2;
		
		for (int i = 0; i < l.size(); i++) {
			to = l.get(i);
			line = str.substring(from, to);
			g.setFont(f);
			g.drawString(line, (w - fm.stringWidth(line)) / 2 ,  y + i * (fh + lineMargin) );
			
			from = to;
		}
		
	}
	
	/**
	 * @param Graphic指明打印的图形环境
	 * @param PageFormat指明打印页格式
	 *            （页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）
	 * @param pageIndex指明页号
	 **/

	public int print(Graphics gra, PageFormat pf, int pageIndex)
			throws PrinterException {
		System.out.println("pageIndex=" + pageIndex);
		// 转换成Graphics2D
		Graphics2D g2 = (Graphics2D) gra;
		// 设置打印颜色为黑色
		g2.setColor(Color.black);
		g = (Graphics2D) gra;
		Paper paper = pf.getPaper();// 得到页面格式的纸张

		double w = paper.getWidth();
		double h = paper.getHeight();
		System.out.println(paper.getWidth());
		System.out.println(paper.getHeight());

		pf.setPaper(paper);// 将该纸张作为格式 */

		// 打印起点坐标
		double x = pf.getImageableX();
		double y = pf.getImageableY();

		switch (pageIndex) {
		case 0:
			// 设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
			// Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput
			Font font = new Font("微软雅黑", Font.PLAIN, 40);
//			g2.setFont(font);// 设置字体
//			FontMetrics fm = g2.getFontMetrics(font);
//
//			float heigth = font.getSize2D();// 字体高度
//			float width = fm.stringWidth(textSeqName);
//
//			System.out.println("x=" + x + ",y=" + y);
//
//			float stringX = (float) (w - width) / 2;
//			float stringY = (float) (h - heigth) / 2;
//
//			System.out.println("stringX=" + stringX + ", stringY=" + stringY);
//
//			
//			g2.drawString(textSeqName, (float) stringX, (float) stringY);
			
			Rectangle rHeader1 = new Rectangle((int)x, (int)y, (int)w, (int)h-300);
			Rectangle rHeader2 = new Rectangle((int)x, (int)y, (int)w, (int)h-100);
			Rectangle rHeader3 = new Rectangle((int)x, (int)y, (int)w, (int)h+200);
			
			drawInRect(rHeader1, new Font("幼圆", Font.PLAIN, 30), "     健康·快乐·富足               HEALTH·HAPPINESS·WEALTH", 8);
			drawInRect(rHeader2, new Font("黑体", Font.PLAIN, 20), "                2017年复星集团年中工作会议                    FUXING NIANZHONG GONGZUOHUIYI", 5);
			drawInRect(rHeader3, new Font("黑体", Font.PLAIN, 40), "殷圣鸽", 10);
			
			return PAGE_EXISTS;
		default:
			return NO_SUCH_PAGE;
		}

	}
	
	private String textSeqMeeting;
	private String textSeqName;

	@Path("/ex/{print_key}/{p1}/{p2}/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String print(@PathParam("print_key") String key,
			@PathParam("p1") String p1
			,@PathParam("p2") String p2
			) {

		Result r = Result.success();

		try {
			
			textSeqMeeting = p1;
			textSeqName = p2;
			// 获取打印服务对象
			job = PrinterJob.getPrinterJob();
			
			PrinterJob.lookupPrintServices();
			
			// 查找所有打印服务
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

			PrintService service = getPS(Arrays.asList(services), key);

			if (service == null) {
				service = PrintServiceLookup.lookupDefaultPrintService();   
			}
			
			if (service != null) {
				
				dumpPrinterInfo(service);
				job.setPrintService(service);
				
				PageFormat pf0 = job.defaultPage();// 得到默认页格式
				PageFormat pf1 = (PageFormat) pf0.clone();
				Paper p = pf0.getPaper();
				p.setImageableArea(0, 0, pf0.getWidth(), pf0.getHeight());
				pf1.setPaper(p);
				
				PageFormat pf2 = job.validatePage(pf1);
				Book book = new Book();
				book.append(this, pf2);
				
				job.setPageable(book);// 设置打印类
				job.print();
			} else {
				throw new IllegalArgumentException("找不到能使用的打印机");
			}
		} catch (Throwable t) {
			r = Result.unknowException(t);
		}

		return r.toString();
	}
	
	private void dumpPrinterInfo(PrintService myPrinter) {
		// TODO Auto-generated method stub
		// 可以输出打印机的各项属性
		AttributeSet att = myPrinter.getAttributes();

		for (Attribute a : att.toArray()) {

			String attributeName;
			String attributeValue;

			attributeName = a.getName();
			attributeValue = att.get(a.getClass()).toString();

			System.out.println(attributeName + " : " + attributeValue);
		}
	}

	private PrintService getPS(List<PrintService> ps, String key) {
		PrintService p = null;
		if (ps != null) {
			for (int i = 0; i < ps.size(); i++) {  
				p = ps.get(i);
				// System.out.println("service found: " + p);  
				String svcName = p.toString();  
				if (svcName.toUpperCase().contains(key.toUpperCase()) 
						|| p.getName().toUpperCase().contains(key.toUpperCase())) {  
					//System.out.println("my printer found: " + svcName);  
					// System.out.println("my printer found: " + p);  
					break;  
				}  
			}  
		}
		
		return p;
	}

}  