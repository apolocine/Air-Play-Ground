package org.amia.play.ihm.ticket;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.amia.playground.dao.impl.TicketRepository;
import org.amia.playground.dto.Ticket;

public class TicketForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TicketTableModel ticketTableModel;
    private JTable ticketTable;
    private JButton nextButton;
    private JButton prevButton;
    private JLabel pageLabel;
    private   List<Ticket> tickets = new ArrayList<Ticket>();
    TicketRepository ticketRepository;
       int page = 0;
		int pagesize = 30;
     public TicketForm (TicketRepository ticketRepository) {
		super();
		this.ticketRepository = ticketRepository;
		
	 	tickets = ticketRepository.fetchTicketsFromDatabase(page,pagesize); 
 
	 	
	 	
	 	
        setLayout(new BorderLayout());
        initTable();
        initPaginationControls();
        menuContextuel();
    }

     
     
     void menuContextuel() {
    	// Créer le menu contextuel
         JPopupMenu contextMenu = new JPopupMenu();
         //JMenuItem menuItemEdit = new JMenuItem("Edit");
         JMenuItem menuItemDelete = new JMenuItem("Delete");
         
       //  contextMenu.add(menuItemEdit);
         contextMenu.add(menuItemDelete);
         
         // Ajouter un MouseListener à JList
      // Ajouter un MouseListener à JTable
         ticketTable.addMouseListener(new MouseAdapter() {
             public void mousePressed(MouseEvent e) {
                 // Vérifier si le clic est un clic droit
                 if (SwingUtilities.isRightMouseButton(e)) {
                     int row = ticketTable.rowAtPoint(e.getPoint());
                     int column = ticketTable.columnAtPoint(e.getPoint());
                     if (row != -1 && column != -1) {
                    	 ticketTable.setRowSelectionInterval(row, row); // Sélectionner la ligne sur laquelle le clic droit a été effectué
                         contextMenu.show(ticketTable, e.getX(), e.getY()); // Afficher le menu contextuel
                     }
                 }
             }
         });
         // Ajouter des actions aux éléments de menu (facultatif)
         // Ajouter des actions aux éléments de menu (facultatif)
//         menuItemEdit.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 int selectedRow = ticketTable.getSelectedRow();
//                 if (selectedRow != -1) {
//                     // Logique pour l'édition de l'élément
//                     System.out.println("Edit: " + ticketTable.getValueAt(selectedRow, 1)); // Affiche la valeur dans la colonne "Name"
//                 }
//             }
//         });
         
         menuItemDelete.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 int selectedRow = ticketTable.getSelectedRow();
                 if (selectedRow != -1) {
                	 try {
						ticketRepository.delete(selectedRow);
						
//                     ((TicketTableModel) ticketTable.getModel()).removeRow(selectedRow); // Supprimer la ligne de la table
                     ticketTableModel.removeTicket(selectedRow); // Supprimer la ligne de la table
               
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  }
             }
         });

     }
    private void initTable() {
        ticketTableModel = new TicketTableModel(tickets );
        ticketTable = new JTable(ticketTableModel);
        add(new JScrollPane(ticketTable), BorderLayout.CENTER);
    }

    private void initPaginationControls() {
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nextButton = new JButton("Next");
        prevButton = new JButton("Previous");
        pageLabel = new JLabel();

        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        prevButton.addActionListener(e -> loadPreviousPage());
        nextButton.addActionListener(e -> loadNextPage());

        add(paginationPanel, BorderLayout.SOUTH);
        
        updatePageLabel() ;
    }

    private void loadNextPage() {
    	page = ticketTableModel.getCurrentPage() + 1;
    	
    	 
    	tickets = ticketRepository.fetchTicketsFromDatabase(page, pagesize);
    	
        ticketTableModel.loadTicketsForPage(page,tickets);
        updatePageLabel();
    }

    private void loadPreviousPage() {
    	page = ticketTableModel.getCurrentPage() - 1;
     
    	tickets = ticketRepository.fetchTicketsFromDatabase(page, pagesize);
        ticketTableModel.loadTicketsForPage(page,tickets);
        updatePageLabel();
    }

    private void updatePageLabel() {
    	if(page<=0) {
    		prevButton.setEnabled(false);
    		
    	}else {
    		prevButton.setEnabled(true);
    	}
    	
    	pageLabel.setText("Page: " + (page));
    }
   



}