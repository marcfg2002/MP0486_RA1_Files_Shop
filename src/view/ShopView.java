package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;
import utils.Constants;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

public class ShopView extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private Shop shop;

    private JPanel contentPane;
    private JButton btnShowCash;
    private JButton btnAddProduct;
    private JButton btnAddStock;
    private JButton btnRemoveProduct;
    private JButton btnExportInventary;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ShopView frame = new ShopView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ShopView() {
        setTitle("MiTenda.com - Menu principal");

        // listen key
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // create shop
        shop = new Shop();
        shop.loadInventory();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Label principal
        JLabel lblShowCash = new JLabel("Seleccione o pulse una opción:");
        lblShowCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblShowCash.setBounds(57, 20, 300, 14);
        contentPane.add(lblShowCash);

        // Botón Exportar Inventario
        btnExportInventary = new JButton("0. Exportar Inventario");
        btnExportInventary.setHorizontalAlignment(SwingConstants.LEFT);
        btnExportInventary.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnExportInventary.setBounds(99, 40, 236, 40);
        contentPane.add(btnExportInventary);
        btnExportInventary.addActionListener(this);

        // Botón Contar caja
        btnShowCash = new JButton("1. Contar caja");
        btnShowCash.setHorizontalAlignment(SwingConstants.LEFT);
        btnShowCash.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnShowCash.setBounds(99, 90, 236, 40);
        contentPane.add(btnShowCash);
        btnShowCash.addActionListener(this);

        // Botón Añadir producto
        btnAddProduct = new JButton("2. Añadir producto");
        btnAddProduct.setHorizontalAlignment(SwingConstants.LEFT);
        btnAddProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnAddProduct.setBounds(99, 140, 236, 40);
        contentPane.add(btnAddProduct);
        btnAddProduct.addActionListener(this);

        // Botón Añadir stock
        btnAddStock = new JButton("3. Añadir stock");
        btnAddStock.setHorizontalAlignment(SwingConstants.LEFT);
        btnAddStock.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnAddStock.setBounds(99, 190, 236, 40);
        contentPane.add(btnAddStock);
        btnAddStock.addActionListener(this);

        // Botón Eliminar producto
        btnRemoveProduct = new JButton("9. Eliminar producto");
        btnRemoveProduct.setHorizontalAlignment(SwingConstants.LEFT);
        btnRemoveProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnRemoveProduct.setBounds(99, 240, 236, 40);
        contentPane.add(btnRemoveProduct);
        btnRemoveProduct.addActionListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == '0') {
            this.openInventoryView(); // Exportar inventario
        } else if (e.getKeyChar() == '1') {
            this.openCashView();
        } else if (e.getKeyChar() == '2') {
            this.openProductView(Constants.OPTION_ADD_PRODUCT);
        } else if (e.getKeyChar() == '3') {
            this.openProductView(Constants.OPTION_ADD_STOCK);
        } else if (e.getKeyChar() == '9') {
            this.openProductView(Constants.OPTION_REMOVE_PRODUCT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExportInventary) {
            this.openInventoryView();
        } else if (e.getSource() == btnShowCash) {
            this.openCashView();
        } else if (e.getSource() == btnAddProduct) {
            this.openProductView(Constants.OPTION_ADD_PRODUCT);
        } else if (e.getSource() == btnAddStock) {
            this.openProductView(Constants.OPTION_ADD_STOCK);
        } else if (e.getSource() == btnRemoveProduct) {
            this.openProductView(Constants.OPTION_REMOVE_PRODUCT);
        }
    }

    /**
     * Abrir dialogo para mostrar la caja
     */
    public void openCashView() {
        CashView dialog = new CashView(shop);
        dialog.setSize(400, 400);
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    /**
     * Exportar inventario con popups de éxito/error
     */
    public void openInventoryView() {
        boolean check = shop.writeInventory();

        if (check) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El inventario se ha exportado correctamente",
                    "Éxito",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Ha ocurrido un error al exportar el inventario",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abrir dialogo para añadir/eliminar/stock productos
     */
    public void openProductView(int option) {
        ProductView dialog = new ProductView(shop, option);
        dialog.setSize(400, 400);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}

