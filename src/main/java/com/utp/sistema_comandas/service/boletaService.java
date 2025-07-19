package com.utp.sistema_comandas.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.utp.sistema_comandas.model.DetallePedido;
import com.utp.sistema_comandas.model.Pedido;

@Service
public class boletaService {

    public byte[] generarBoletaPDF(Pedido pedido) throws Exception {
        Document documento = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(documento, out);

        documento.open();

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        documento.add(new Paragraph("BOLETA DE CONSUMO", tituloFont));
        documento.add(new Paragraph("Fecha: " + pedido.getFechaHora(), textoFont));
        documento.add(new Paragraph("Mesa: " + pedido.getMesa().getNumero(), textoFont));
        documento.add(new Paragraph("Mozo: " + pedido.getMozo().getNombre() + " " + pedido.getMozo().getApellido(),
                textoFont));
        documento.add(new Paragraph("Cliente: " + pedido.getMesa().getNombreCliente(), textoFont));
        documento.add(new Paragraph("Cantidad de comensales: " + pedido.getMesa().getCantidadPersonas(), textoFont));
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.addCell("Producto");
        tabla.addCell("Cantidad");
        tabla.addCell("Subtotal");

        double total = 0;

        for (DetallePedido detalle : pedido.getDetalles()) {
            tabla.addCell(detalle.getProducto().getNombre());
            tabla.addCell(String.valueOf(detalle.getCantidad()));
            tabla.addCell(String.format("S/ %.2f", detalle.getSubtotal()));
            total += detalle.getSubtotal();
        }

        documento.add(tabla);
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("TOTAL: S/ " + String.format("%.2f", total), tituloFont));

        documento.close();
        return out.toByteArray();
    }
}
