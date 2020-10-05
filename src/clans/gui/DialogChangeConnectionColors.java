package clans.gui;

import java.text.DecimalFormat;

import javax.swing.JTextField;

public class DialogChangeConnectionColors extends javax.swing.JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1895643871437384612L;

    public DialogChangeConnectionColors(ProgramWindow parent) {
        super(parent, false);
        
        this.parent = parent;
        
        this.colorarr = parent.data.colorarr;
        
		draw1 = new drawpanel(colorarr);
        
        initComponents();
        
        draw1.drawwidth = mainpanel.getWidth();
        draw1.drawheight = mainpanel.getHeight();
        draw1.elementwidth = ((float) draw1.drawwidth) / ((float) colorarr.length);
		
		if (parent.data.blasthits != null) {
			data2d = false;
			
			if (parent.data.usescval) { // SC values
				for (int i = 0; i < fields.length; i++) {
					setTextField(fields[i], scValueToExponent(parent.data.colorcutoffs[i]));
				}

			} else { // p-values
				for (int i = 0; i < fields.length; i++) {
					setTextField(fields[i], pValueToExponent(parent.data.colorcutoffs[i]));
				}
			}

		} else { // attraction values
			data2d = true;

			for (int i = 0; i < fields.length; i++) {
				setTextField(fields[i], attractionValueToExponent(parent.data.colorcutoffs[i]));
			}
		}
		
		this.setVisible(true);
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        mainpanel = new javax.swing.JPanel();
        buttonpanel = new javax.swing.JPanel();
        worstlabel = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        colorGradientButton = new javax.swing.JButton();
        valueGradientButton = new javax.swing.JButton();
        closebutton = new javax.swing.JButton();
        bestlabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        infotextfield = new javax.swing.JTextField();
        textfieldpanel = new javax.swing.JPanel();
        
        
        fields = new JTextField[NUMBER_OF_COLOR_PATCHES];
        
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new javax.swing.JTextField();
		}

        setTitle("Change Colors");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                handleDialogClosing();
            }
        });

        mainpanel.setLayout(new javax.swing.BoxLayout(mainpanel, javax.swing.BoxLayout.X_AXIS));

        mainpanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        mainpanel.setPreferredSize(new java.awt.Dimension(400, 20));
        mainpanel.add(draw1);
        mainpanel.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                handleDialogResizing();
            }
        });
        mainpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mainpanelMouseReleased(evt);
            }
        });

        getContentPane().add(mainpanel, java.awt.BorderLayout.CENTER);

        buttonpanel.setLayout(new java.awt.GridLayout(1, 0));

        worstlabel.setText("Worst");
        buttonpanel.add(worstlabel);

        updateButton.setText("UPDATE");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateValuesFromTextfields();
            }
        });

        buttonpanel.add(updateButton);

        colorGradientButton.setText("Color gradient");
        colorGradientButton.setToolTipText("select the worst and best color, rest is computed");
        colorGradientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateColorGradient();
            }
        });

        buttonpanel.add(colorGradientButton);

        valueGradientButton.setText("Value gradient");
        valueGradientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateValueGradient();
            }
        });

        buttonpanel.add(valueGradientButton);

        closebutton.setText("CLOSE");
        closebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebuttonActionPerformed(evt);
            }
        });

        buttonpanel.add(closebutton);

        bestlabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        bestlabel.setText("Best");
        bestlabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        buttonpanel.add(bestlabel);

        getContentPane().add(buttonpanel, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(new java.awt.GridLayout(2, 0));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        infotextfield.setEditable(false);
        infotextfield.setText("Connections with P-value better than 1E-X are drawn in the corresponding color");
        jPanel2.add(infotextfield);

        jPanel1.add(jPanel2);

        textfieldpanel.setLayout(new java.awt.GridLayout(1, 0));

		for (int i = 0; i < fields.length; i++) {
			textfieldpanel.add(fields[i]);
		}
    	
        jPanel1.add(textfieldpanel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        pack();
    }

    private static void setTextField(JTextField field, float number) {
    	field.setText(fieldFloatFormat.format(number));
    }
    
    /**
     * Calculates a gradient between the values in the "worst" and "best" text fields. 
     */
    private void calculateValueGradient() {
        String tmpstr = "";
        float worst, best;
		
        try {
        	// first field == worst
			tmpstr = fields[0].getText();
			worst = Float.parseFloat(tmpstr);
			
			// last field == best
			tmpstr = fields[fields.length - 1].getText();
			best = Float.parseFloat(tmpstr);

		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to parse float from '" + tmpstr + "'");
			return;
		}
        
		float interval = (best - worst) / (fields.length - 1);

		float current_value;
		// set all except for first and last, which determined the interval above
		for (int i = 1; i < fields.length - 1; i++) {
			current_value = worst + i * interval;
			
			setTextField(fields[i], current_value);

			if (parent.data.blasthits != null) {
				if (parent.data.usescval) { // SC values
					parent.data.colorcutoffs[i] = exponentToScValue(fields[i].getText());

				} else { // p-values
					parent.data.colorcutoffs[i] = exponentToPValue(fields[i].getText());
				}

			} else { // attraction values
				parent.data.colorcutoffs[i] = exponentToAttractionValue(fields[i].getText());
			}
		}

		repaint();
		completelyRepaintParent();
    }

	/**
	 * Calculates color gradient based on the "worst" and "best" colors.
	 */
    private void calculateColorGradient() {
        // calculate a color gradient
        int worstred = colorarr[0].getRed();
        int worstgreen = colorarr[0].getGreen();
        int worstblue = colorarr[0].getBlue();
        
        int bestred = colorarr[colorarr.length - 1].getRed();
        int bestgreen = colorarr[colorarr.length - 1].getGreen();
        int bestblue = colorarr[colorarr.length - 1].getBlue();

        // now compute the stepwise gradient
        float redstep = ((float) (bestred - worstred)) / ((float) colorarr.length);
        float greenstep = ((float) (bestgreen - worstgreen)) / ((float) colorarr.length);
        float bluestep = ((float) (bestblue - worstblue)) / ((float) colorarr.length);
        
        for (int i = 1; i < colorarr.length - 1; i++) {
			colorarr[i] = new java.awt.Color((int) (worstred + (i * redstep)), (int) (worstgreen + (i * greenstep)),
					(int) (worstblue + (i * bluestep)));
        }
        
        repaint();
        completelyRepaintParent();
    }

    private void closebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebuttonActionPerformed
        // Add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closebuttonActionPerformed

    
	/**
	 * Converts the cutoff exponent to the real cutoff p-value. This inverts {@code pValueToExponent}.
	 * 
	 * @param value
	 *            The cutoff exponent.
	 * @return The p-value corresponding to the cutoff exponent.
	 * @throws NumberFormatException
	 *             If the entered number is not parsable as Float.
	 */
	private float exponentToPValue(String value) throws NumberFormatException {
		return (float) ((Float.parseFloat(value) * ln10 - parent.data.p2attoffset) / parent.data.p2attfactor);
	}

	/**
	 * Extracts the exponent from a p-value. This inverts method {@code exponentToPValue}.
	 * 
	 * @param value
	 *            The p-value.
	 * @return The exponent representation.
	 */
	private float pValueToExponent(float value) {
		return (float) (((value * parent.data.p2attfactor) + parent.data.p2attoffset) / ln10);
	}

	/**
	 * Converts the cutoff exponent to the real cutoff SC value. This inverts {@code scValueToExponent}.
	 * 
	 * @param value
	 *            The cutoff exponent.
	 * @return The SC value corresponding to the cutoff exponent.
	 * @throws NumberFormatException
	 *             If the entered number is not parsable as Float.
	 */
	private float exponentToScValue(String value) throws NumberFormatException {
		return (float) ((Float.parseFloat(value) - parent.data.p2attoffset) / parent.data.p2attfactor);
	}

	/**
	 * Extracts the exponent from a SC value. This inverts method {@code exponentToScValue}.
	 * 
	 * @param value
	 *            The SC value.
	 * @return The exponent representation.
	 */
	private int scValueToExponent(float value) {
		return (int) (((value * parent.data.p2attfactor) + parent.data.p2attoffset) / ln10);
	}

	/**
	 * Converts the cutoff exponent to the real cutoff attraction value. This inverts {@code attractionValueToExponent}.
	 * 
	 * @param value
	 *            The cutoff exponent.
	 * @return The attraction value corresponding to the cutoff exponent.
	 * @throws NumberFormatException
	 *             If the entered number is not parsable as Float.
	 */
	private float exponentToAttractionValue(String value) throws NumberFormatException {
		return (float) (Float.parseFloat(value) / parent.data.p2attfactor);
	}

	/**
	 * Extracts the exponent from a attraction value. This inverts method {@code exponentToAttractionValue}.
	 * 
	 * @param value
	 *            The attraction value.
	 * @return The exponent representation.
	 */
	private float attractionValueToExponent(float value) {
		return value * parent.data.p2attfactor;
	}
    
	/**
	 * Converts exponent {@code value} to a p-value and updates the color cutoff accordingly.
	 * 
	 * @param value
	 *            The input value.
	 * @param index
	 *            The index to the color cutoff array item that must be adjusted.
	 */
	private void verifyAndSetPValue(String value, int index) {
		float new_value;
		try {
			new_value = exponentToPValue(value);

		} catch (NumberFormatException e) {
			System.err.println("unable to parse float from " + value);
			return;
		}

		setTextField(fields[index], pValueToExponent(new_value));
	}

	/**
	 * Converts exponent {@code value} to a SC value and updates the color cutoff accordingly.
	 * 
	 * @param value
	 *            The input value.
	 * @param index
	 *            The index to the color cutoff array item that must be adjusted.
	 */
	private void verifyAndSetSCValue(String value, int index) {
		float new_value;
		try {
			new_value = exponentToScValue(value);

		} catch (NumberFormatException e) {
			System.err.println("unable to parse float from " + value);
			return;
		}

		setTextField(fields[index], scValueToExponent(new_value));
	}

	/**
	 * Converts {@code value} to an attraction value and updates the color cutoff accordingly.
	 * 
	 * @param value
	 *            The input value.
	 * @param index
	 *            The index to the color cutoff array item that must be adjusted.
	 */
	private void verifyAndSetAttractionValue(String value, int index) {
		float new_value;
		try {
			new_value = exponentToAttractionValue(value);

		} catch (NumberFormatException e) {
			System.err.println("unable to parse float from " + value);
			return;
		}

		setTextField(fields[index], attractionValueToExponent(new_value));
	}
    
	/**
	 * Reads text fields and injects their data into the model if parsable.
	 */
	private void updateValuesFromTextfields() {

		if (!data2d) {
			if (parent.data.usescval) { // SC values
				for (int i = 0; i < fields.length; i++) {
					verifyAndSetSCValue(fields[i].getText(), i);
				}

			} else { // p-values
				for (int i = 0; i < fields.length; i++) {
					verifyAndSetPValue(fields[i].getText(), i);
				}
			}

		} else { // attraction values
			for (int i = 0; i < fields.length; i++) {
				verifyAndSetAttractionValue(fields[i].getText(), i);
			}
		}

		completelyRepaintParent();
	}

	/**
	 * Opens a color chooser dialog for a clicked element of the color bar and handles the user input.
	 * 
	 * @param evt
	 */
	private void mainpanelMouseReleased(java.awt.event.MouseEvent evt) {

		int xval = evt.getX();
		int colorelement = (int) (xval / draw1.elementwidth); // determine clicked element

		colorarr[colorelement] = parent.safe_change_color_dialog("Select New Color", colorarr[colorelement]);

		repaint();
		completelyRepaintParent();
	}

	/**
	 * Handle dialog resizing.
	 */
    private void handleDialogResizing() {
        draw1.drawwidth = mainpanel.getWidth();
        draw1.drawheight = mainpanel.getHeight();
        draw1.elementwidth = ((float) draw1.drawwidth) / ((float) colorarr.length);
        repaint();
    }
    
	/**
	 * Triggers a reevaluation and repaint of the data in the parent.
	 */
    private void completelyRepaintParent() {
		parent.data.resetDrawOrder();
		parent.repaint();
    }

	/**
	 * Handle dialog closing.
	 * 
	 * @param evt
	 */
	private void handleDialogClosing() {
		setVisible(false);
		dispose();
	}
    
    java.awt.Color[] colorarr = new java.awt.Color[2];
    drawpanel draw1;
    javax.swing.JColorChooser colorchooser = new javax.swing.JColorChooser();
    ProgramWindow parent;
    static double ln10 = java.lang.Math.log(10);
    boolean data2d = false;

    private javax.swing.JLabel bestlabel;
    private javax.swing.JPanel buttonpanel;
    private javax.swing.JButton closebutton;
    
    private final int NUMBER_OF_COLOR_PATCHES = 10;
    private javax.swing.JTextField[] fields;
    private final static DecimalFormat fieldFloatFormat = new DecimalFormat("#.##");
    
    private javax.swing.JButton colorGradientButton;
    private javax.swing.JTextField infotextfield;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JPanel textfieldpanel;
    private javax.swing.JButton updateButton;
    private javax.swing.JButton valueGradientButton;
    private javax.swing.JLabel worstlabel;

    class drawpanel extends javax.swing.JPanel {

        private static final long serialVersionUID = 5838939531675395908L;

        public drawpanel(java.awt.Color[] colorarr) {
            this.colorarr = colorarr;
        }
        
        java.awt.Color[] colorarr;
        float elementwidth;
        int drawwidth;
        int drawheight;

        public void paintComponent(java.awt.Graphics g) {
            for (int i = 0; i < colorarr.length; i++) {
            	g.setColor(colorarr[i]);
                g.fillRect((int) (i * elementwidth), 0, (int) elementwidth, drawheight);
                
                g.setColor(java.awt.Color.black);
                g.drawRect((int) (i * elementwidth), 0, (int) elementwidth, drawheight);
            }
        }
    }
}