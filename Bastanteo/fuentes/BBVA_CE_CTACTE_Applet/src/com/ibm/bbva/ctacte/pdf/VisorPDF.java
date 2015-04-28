package com.ibm.bbva.ctacte.pdf;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VisorPDF extends JPanel {
	
	private static final float ROTACION = 0F;
	private static final float ESCALA_INICIAL = 1F;
	private static final float VARIACION_ESCALA = 0.1F;
	private static final float MINIMO_ESCALA = 0.1F;
	private static final float MAXIMO_ESCALA = 10F;
	private static final float TAMANIO_NORMAL = 100F;
	
	private JFormattedTextField txtPagina;
	private JFormattedTextField txtZoom;
	private JLabel lblPagina;
	
	private Document document = new Document();
	private ImageIcon[] imagenes;
	private int paginaActual;
	private float zoomActualF;
	private String zoomActualS;
	private boolean tienePDF;
	
	public VisorPDF () {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		txtZoom = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtZoom.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				int key = event.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					aceptarZoom();
				}
			}
		});
		txtZoom.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				actualizarZoom ();
			}
		});
		
		JButton btnReducir = new JButton("-");
		btnReducir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reducir();
			}
		});
		GridBagConstraints gbc_btnReducir = new GridBagConstraints();
		gbc_btnReducir.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnReducir.insets = new Insets(0, 0, 0, 5);
		gbc_btnReducir.gridx = 0;
		gbc_btnReducir.gridy = 0;
		panel.add(btnReducir, gbc_btnReducir);
		txtZoom.setText("zoom");
		GridBagConstraints gbc_txtZoom = new GridBagConstraints();
		gbc_txtZoom.insets = new Insets(0, 0, 0, 5);
		gbc_txtZoom.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtZoom.gridx = 1;
		gbc_txtZoom.gridy = 0;
		panel.add(txtZoom, gbc_txtZoom);
		txtZoom.setColumns(4);
		
		JButton btnCrecer = new JButton("+");
		btnCrecer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				crecer ();
			}
		});
		
		JLabel label = new JLabel("%");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		GridBagConstraints gbc_btnCrecer = new GridBagConstraints();
		gbc_btnCrecer.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnCrecer.insets = new Insets(0, 0, 0, 25);
		gbc_btnCrecer.gridx = 3;
		gbc_btnCrecer.gridy = 0;
		panel.add(btnCrecer, gbc_btnCrecer);
		
		JButton btnAnterior = new JButton("<");
		btnAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				anterior ();
			}
		});
		GridBagConstraints gbc_btnAnterior = new GridBagConstraints();
		gbc_btnAnterior.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnAnterior.insets = new Insets(0, 0, 0, 5);
		gbc_btnAnterior.gridx = 4;
		gbc_btnAnterior.gridy = 0;
		panel.add(btnAnterior, gbc_btnAnterior);
		
		txtPagina = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtPagina.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				actualizarPagina();
			}
		});
		txtPagina.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				int key = event.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					irPagina ();
				}
			}
		});
		txtPagina.setText("pagina");
		GridBagConstraints gbc_txtPagina = new GridBagConstraints();
		gbc_txtPagina.anchor = GridBagConstraints.WEST;
		gbc_txtPagina.insets = new Insets(0, 0, 0, 5);
		gbc_txtPagina.gridx = 5;
		gbc_txtPagina.gridy = 0;
		panel.add(txtPagina, gbc_txtPagina);
		txtPagina.setColumns(4);
		
		JButton btnSiguiente = new JButton(">");
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				siguiente ();
			}
		});
		GridBagConstraints gbc_btnSiguiente = new GridBagConstraints();
		gbc_btnSiguiente.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSiguiente.gridx = 6;
		gbc_btnSiguiente.gridy = 0;
		panel.add(btnSiguiente, gbc_btnSiguiente);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		lblPagina = new JLabel();
		lblPagina.setBorder(border);
		scrollPane.setViewportView(lblPagina);
	}

	private void actualizarPagina() {
		txtPagina.setText(String.valueOf(paginaActual+1));
	}

	private void irPagina() {
		try {
			int pagina = Integer.parseInt(txtPagina.getText())-1;
			if (pagina < imagenes.length && pagina >= 0) {
				cambiarPagina(pagina);
			} else {
				actualizarPagina();
			}
		} catch (NumberFormatException e) {
			actualizarPagina();
		}
	}

	private void siguiente() {
		int pagina = paginaActual + 1;
		if (pagina < imagenes.length) {
			cambiarPagina(pagina);
		}
	}

	private void anterior() {
		int pagina = paginaActual - 1;
		if (pagina >= 0) {
			cambiarPagina(pagina);
		}
	}

	private void aceptarZoom() {
		try {
			float zoom = Integer.parseInt(txtZoom.getText()) / TAMANIO_NORMAL;
			if (zoom >= MINIMO_ESCALA && zoom <= MAXIMO_ESCALA) {
				cambiarZoomFloat(zoom);
			} else {
				actualizarZoom();
			}
		} catch (Exception e) {
			actualizarZoom();
		}
	}

	private void actualizarZoom() {
		txtZoom.setText(zoomActualS);
	}

	private void reducir() {
		float zoom = zoomActualF - VARIACION_ESCALA;
		if (zoom >= MINIMO_ESCALA) {
			cambiarZoomFloat(zoom);
		}
	}

	private void crecer() {
		float zoom = zoomActualF + VARIACION_ESCALA;
		if (zoom <= MAXIMO_ESCALA) {
			cambiarZoomFloat(zoom);
		}
	}
	
	public void cargarPDF (String direccion, float escala) throws IOException, PDFException, PDFSecurityException {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("direccion: "+direccion);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("escala: "+escala);
		document.setFile(direccion);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Locacion del documento: "+document.getDocumentLocation());
		imagenes = new ImageIcon[document.getNumberOfPages()];
		
		cambiarZoomFloat (escala);
		cambiarPagina (0);
		tienePDF = true;
	}
	
	private void cambiarPagina (int pagina) {
		paginaActual = pagina;
		txtPagina.setText(String.valueOf(paginaActual+1));
		ImageIcon imagen = imagenes[pagina];
		if (imagen == null) {
			imagen = obtenerImagen (pagina);
			imagenes[pagina] = imagen;
		}
		lblPagina.setIcon(imagen);
	}
	
	private void cambiarZoomFloat (float zoom) {
		if (zoomActualF != zoom) {
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("entro al if (zoomActualF != zoom) {");
			zoomActualF = zoom;
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("zoomActualF: "+zoomActualF);
			int iZoom = (int)(TAMANIO_NORMAL * zoom);
			zoomActualS = String.valueOf(iZoom);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("zoomActualS: "+zoomActualS);
			txtZoom.setText(zoomActualS);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("txtZoom.setText(zoomActualS);");
			imagenes = new ImageIcon[imagenes.length];
			
			imagenes[paginaActual] = obtenerImagen (paginaActual);
			ImageIcon imagen = imagenes[paginaActual];
			lblPagina.setIcon(imagen);
		}
	}
	
	private ImageIcon obtenerImagen (int pagina) {
		BufferedImage bufferedImage = (BufferedImage) document.getPageImage(pagina, GraphicsRenderingHints.SCREEN, 
				Page.BOUNDARY_CROPBOX, ROTACION, zoomActualF);
		return new ImageIcon(bufferedImage);
	}
	
	public void cerrar(){
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cerrar");
        document.dispose();		
	}

	public boolean isTienePDF() {
		return tienePDF;
	}
}
