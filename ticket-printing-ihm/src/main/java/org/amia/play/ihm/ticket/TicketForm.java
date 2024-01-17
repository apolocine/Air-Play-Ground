package org.amia.play.ihm.ticket;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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