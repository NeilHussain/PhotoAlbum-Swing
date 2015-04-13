package cs213.photoAlbum.guiview;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cs213.photoAlbum.control.AlbumEditorController;
import cs213.photoAlbum.control.BackendController;
import cs213.photoAlbum.control.PhotoEditorController;
import cs213.photoAlbum.control.UserEditorController;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.CollectionListener;
import cs213.photoAlbum.util.CollectionView;
import cs213.photoAlbum.util.CustomPanel;
import cs213.photoAlbum.util.HintTextField;
import cs213.photoAlbum.util.ImageUtilities;
import cs213.photoAlbum.util.MenuButton;
import cs213.photoAlbum.util.MenuButtonsContainer;
import cs213.photoAlbum.util.PhotoViewer;
import cs213.photoAlbum.util.RoundedCornerBorder;
import cs213.photoAlbum.util.UserIcon;

public class GuiView extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String containerFolder = "data/AppPhotos";
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/y-kk:mm:ss",
			Locale.US);

	static CollectionView allPhotosCollection;
	static CollectionView albumsCollection;
	static CollectionView yearsCollection;
	static CollectionView singleAlbumCollection;
	static CollectionView searchCollection;

	static boolean singleAlbum = false;
	static boolean searching = false;
	static boolean viewing = false;

	static int frameWidth = 1100;
	static int frameHeight = 700;

	static int thumbWidth = 200;
	static int thumbHeight = 150;

	// Initial scale of the images (%)
	static int initScale = 75;

	static String[] labels = { "photos", "albums", "years" };

	static int menuButtonWidth = 125;
	static int menuButtonHeight = 75;

	static int detailsMenuHeight = 50;

	static int albumsDensity = 4;

	static GuiView gui;

	static final String nameLabelConstant = "Name: ";
	static final String captionLabelConstant = "Caption: ";
	static final String tagsLabelConstant = "Tags: ";
	static final String dateLabelConstant = "Date: ";

	static String file;
	static String directoryPath;
	static String albumName = "";

	// ######### Controllers ############//
	static BackendController backend = new BackendController();
	static UserEditorController user;
	static AlbumEditorController album;
	static PhotoEditorController photo;

	static JFrame frame;
	static JWindow addPhoto_window;
	static JWindow addAlbum_window;
	static JWindow settings_window;
	static JWindow edit_photo_window;
	static JWindow edit_album_window;
	static JWindow login_window;
	static JWindow admin_window;
	static JWindow photo_viewer_window;
	static JWindow delete_album_window;
	static JWindow delete_photo_window;
	static JPanel glass;

	// ###### JPanels ##########/
	static JPanel contentCardPanel;
	static CardLayout contentCardLayout;
	static JPanel allPhotosPanel;
	static JPanel albumPhotosPanel;
	static JPanel yearPhotosPanel;

	static JPanel mainContent;

	static JPanel mainCardPanel;// Panel to hold everything
	static CardLayout mainCardLayout;

	static JPanel bottomDetailPanel;
	static CardLayout bottomDetailLayout;

	// labels and buttons for details view

	// Photos
	static JLabel photoNameLabel = new JLabel();
	static JLabel photoCaptionLabel = new JLabel();
	static JLabel photoTagsLabel = new JLabel();
	static JLabel photosEditButton = new JLabel();
	static JLabel photoDateLabel = new JLabel();
	// Albums
	static JLabel albumNameLabel = new JLabel();
	static JLabel albumCaptionLabel = new JLabel();
	static JLabel albumNumberLabel = new JLabel();
	static JLabel albumEditButton = new JLabel();
	static JLabel albumDatesLabel = new JLabel();
	// Years
	static JLabel yearNameLabel = new JLabel();
	static JLabel yearCaptionLabel = new JLabel();
	static JLabel yearTagsLabel = new JLabel();
	static JLabel yearDateLabel = new JLabel();

	// Search
	static JLabel searchNameLabel = new JLabel();
	static JLabel searchCaptionLabel = new JLabel();
	static JLabel searchTagsLabel = new JLabel();
	static JLabel searchDateLabel = new JLabel();
	static JLabel searchMakeAlbumButton = new JLabel();

	// ########## Objects ################//
	static MenuButtonsContainer menu;
	static JSlider slider;
	static JLabel backLabel;
	static UserIcon icon;
	static JLabel searchLabel;
	static HintTextField search;

	static JList<String> aList;

	static Photo[] searchResults;

	static BufferedImage[] allThumbnails;
	static BufferedImage[] albumThumbnails;
	static BufferedImage[] sortedThumbnails;
	static DefaultListModel<String> tagsListModel;

	GuiView() {
		// ########################
		// Need to revamp this method to build the three collection views!
		// using the controllers we made before
		// ########################

		// this.allPhotosCollection = null;
		// This should get all the photo thumbnails

		// Temp stuff for debugging and testing

		BuildInitCollections(this, false);

		allPhotosPanel.removeAll();

		albumPhotosPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		yearPhotosPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		albumPhotosPanel.add(albumsCollection);

		// albumPhotosPanel.add(bottomAlbumDetailsPanel(), BorderLayout.SOUTH);

		yearPhotosPanel.add(yearsCollection);

		allPhotosPanel.add(allPhotosCollection);
		// collection.setScale(1.0f);

	}

	private static void BuildInitCollections(GuiView guiview, boolean reuse) {

		int numPhotos = user.numPhotos();
		File[] thumbnailFilePaths = user.getAllThumbnails();

		BufferedImage allPhotos[];
		if (false) {

		} else {

			allPhotos = new BufferedImage[numPhotos];
			try {

				for (int i = 0; i < numPhotos; i++) {
					allPhotos[i] = ImageIO.read(thumbnailFilePaths[i]);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			allPhotosCollection = buildPhotoCollection(allPhotos, "all photos",
					null, guiview);
		}

		File[] yearThumbnailFilePaths = user.getThumbnails(user.sortPhotos());

		try {

			for (int i = 0; i < numPhotos; i++) {
				allPhotos[i] = ImageIO.read(yearThumbnailFilePaths[i]);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int[] years = user.getYearsArray();

		if (years.length > 0) {
			yearsCollection = buildPhotoCollection(allPhotos, "years", years,
					guiview);
		} else {
			yearsCollection = buildPhotoCollection(allPhotos, "years", null,
					guiview);

		}
		yearsCollection.setScale(.35f);

		// ##################################
		BufferedImage albumPhotos[] = null;
		try {
			albumPhotos = user.getAlbumThumbnails(albumsDensity, thumbWidth,
					thumbHeight);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		albumsCollection = buildPhotoCollection(albumPhotos, "all albums",
				null, guiview);

	}

	private static CollectionView buildPhotoCollection(BufferedImage[] photos,
			String tag, int[] years, GuiView guiview) {

		// operating on user;
		CollectionView collection = null;

		// This should mostly be the same (except thumbnails don't need to be
		// generated again, just loaded from the folder)

		try {
			if (years == null) {
				collection = new CollectionView(
						ImageUtilities.getMultipleThumbnails(photos,
								thumbWidth, thumbHeight), thumbWidth,
						thumbHeight, contentCardPanel.getWidth(), contentCardPanel.getHeight());
			} else {
				collection = new CollectionView(
						ImageUtilities.getMultipleThumbnails(photos,
								thumbWidth, thumbHeight), thumbWidth,
						thumbHeight, contentCardPanel.getWidth(), contentCardPanel.getHeight(), years);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		collection.setSize(frameWidth, frameHeight);
		collection.setTag(tag);

		// collection.setSpacing(2);

		CollectionListener cl = new CollectionListener(collection);
		cl.addObserver(guiview);
		collection.addMouseListener(cl);
		collection.addMouseMotionListener(cl);
		collection.addMouseWheelListener(cl);

		collection.setScale((float) initScale / 100);

		return collection;

	}

	public static void main(String[] args) {

		// GuiView main = new GuiView();
		// allPhotosCollection = buildTempCollectionView();

		frame = buildFrame();

		frame.setVisible(true);

		buildLoginWindow();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				backend.save();
			}
		}));

	}

	private static JFrame buildFrame() {

		// ############ Common JFrame initializing things. #################//
		JFrame main = new JFrame("Photo Album");

		main.setLayout(new BorderLayout(0, 0));
		main.setBackground(Color.WHITE);
		main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		main.setPreferredSize(new Dimension(frameWidth, frameHeight));

		// ############# Login screen setup ###################//

		JPanel loginContent = loginScreenPanel();

		// ############ Main Program Content Laying out ################//

		mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout(0, 0));

		mainContent.add(createMenu(), BorderLayout.NORTH);
		mainContent.add(mainContentPanel(), BorderLayout.CENTER);

		mainContent.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				// Component c = (Component) evt.getSource();
				if (albumsCollection != null) {
					albumsCollection.resized(contentCardPanel.getWidth(), contentCardPanel.getHeight());
				}

				if (yearsCollection != null) {
					yearsCollection.resized(contentCardPanel.getWidth(), contentCardPanel.getHeight());
				}

				if (singleAlbumCollection != null) {
					singleAlbumCollection.resized(contentCardPanel.getWidth(), contentCardPanel.getHeight());
				}

				if (allPhotosCollection != null) {
					allPhotosCollection.resized(contentCardPanel.getWidth(), contentCardPanel.getHeight());
				}

				frameWidth = mainContent.getWidth();
				frameHeight = mainContent.getHeight();

			}
		});

		// ########## Card layout for the bottom detail panel ################
		bottomDetailPanel = new JPanel();// Panel to hold everything
		bottomDetailLayout = new CardLayout();// create CardLayout
												// object
		bottomDetailPanel.setLayout(bottomDetailLayout);

		bottomDetailPanel.add(bottomPhotoDetailsPanel(), labels[0]);
		bottomDetailPanel.add(bottomAlbumDetailsPanel(), labels[1]);
		bottomDetailPanel.add(bottomYearsDetailsPanel(), labels[2]);
		bottomDetailPanel.add(bottomSearchDetailsPanel(), "search");

		bottomDetailLayout.show(bottomDetailPanel, labels[0]);

		mainContent.add(bottomDetailPanel, BorderLayout.SOUTH);

		// ################## MAIN Card Layout Stuff #####################//

		mainCardPanel = new JPanel();// Panel to hold everything
		mainCardLayout = new CardLayout();// create CardLayout object
		mainCardPanel.setLayout(mainCardLayout);
		mainCardPanel.add(mainContent, "main");
		mainCardPanel.add(loginContent, "login");
		mainCardLayout.show(mainCardPanel, "login");

		// ################ Common Drawing stuff #####################//

		main.getContentPane().add(BorderLayout.CENTER, mainCardPanel);
		main.pack();
		glass = createGlassPane(main);

		return main;
	}

	private static Component bottomSearchDetailsPanel() {
		// String nameLabelConstant = "Name: ";
		// String captionLabelConstant = "Caption: ";
		// String tagsLabelConstant = "Tags: ";

		/*
		 * JLabel photoNameLabel = new JLabel(); JLabel photoCaptionLabel = new
		 * JLabel(); JLabel photoTagsLabel = new JLabel(); JButton
		 * photosEditButton = new JButton();
		 */

		// static JLabel searchNameLabel = new JLabel();
		// static JLabel searchCaptionLabel = new JLabel();
		// static JLabel searchTagsLabel = new JLabel();
		// static JLabel searchDateLabel = new JLabel();
		// static JButton searchMakeAlbumButton = new JButton("Make Album");

		searchMakeAlbumButton.setPreferredSize(new Dimension(125, 50));
		ImageIcon editIcon = new ImageIcon(containerFolder + "/makeAlbumButton.png");
		searchMakeAlbumButton.setIcon(editIcon);
		searchMakeAlbumButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				ActionCatchers.makeAlbumButtonClicked();
			}
		});

		CustomPanel detailsPanel = new CustomPanel();
		detailsPanel.setReversed(true);

		JPanel detailsSubPanel = new JPanel();
		// detailsPanel.add(Box.createRigidArea(new Dimension(detailsMenuHeight,
		// detailsMenuHeight)));

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		detailsPanel.setLayout(gridbag);

		GridBagLayout gridbagSub = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		detailsSubPanel.setLayout(gridbagSub);

		// detailsPanel.add(Box.createRigidArea(new Dimension(detailsMenuHeight,
		// detailsMenuHeight)));

		searchNameLabel.setText(nameLabelConstant + "Poop");
		searchCaptionLabel.setText(captionLabelConstant + "Poop");
		searchTagsLabel.setText(tagsLabelConstant + "Poop");
		searchDateLabel.setText(dateLabelConstant + "Poop");

		c.ipadx = 40;
		c.insets = new Insets(5, 50, 5, 50);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(searchNameLabel, c);
		detailsSubPanel.add(searchNameLabel, c);
		// detailsPanel.add(Box.createHorizontalStrut(100),c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		c.ipadx = 40;
		c.weightx = 1.0;
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(searchCaptionLabel, c);
		detailsSubPanel.add(searchCaptionLabel);

		c.gridx++;
		c.insets = new Insets(5, 50, 5, 50);
		c.anchor = GridBagConstraints.WEST;
		detailsSubPanel.add(searchDateLabel, c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		// c.insets = new Insets(5, 50, 5, 0);
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(searchTagsLabel, c);
		// detailsPanel.add(Box.createHorizontalStrut(100),c);
		detailsSubPanel.add(searchTagsLabel, c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		// con.insets = new Insets(0, 0, 0, 50);
		con.insets = new Insets(0, 0, 0, 0);
		con.ipadx = 40;
		con.anchor = GridBagConstraints.EAST;
		con.weightx = 1.0;
		con.gridx = 2;
		con.gridy = 0;
		gridbag.setConstraints(searchMakeAlbumButton, con);
		detailsPanel.add(searchMakeAlbumButton, con);

		con.anchor = GridBagConstraints.WEST;
		con.weightx = 1.0;
		con.gridx = 0;
		con.gridy = 0;
		gridbag.setConstraints(detailsSubPanel, con);
		detailsPanel.add(detailsSubPanel, con);

		searchNameLabel.setVisible(false);
		searchCaptionLabel.setVisible(false);
		searchTagsLabel.setVisible(false);
		// searchMakeAlbumButton.setVisible(false);
		searchDateLabel.setVisible(false);

		return detailsPanel;
	}

	public static JPanel loginScreenPanel() {
		JPanel loginContent = new JPanel();
		loginContent.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		ImageIcon loginIcon = new ImageIcon(containerFolder + "/background.png");
		JLabel loginLabel = new JLabel(loginIcon);
		loginContent.add(loginLabel, BorderLayout.CENTER);

		return loginContent;

	}

	public static JPanel mainContentPanel() {

		allPhotosPanel = allPhotoContentPanel();

		albumPhotosPanel = albumsContentPanel();

		yearPhotosPanel = yearsContentPanel();

		// ########### Content card layout for main content #############
		// Shouldn't have to touch this anymore (Unless a tab is added)

		contentCardPanel = new JPanel();// Panel to hold everything
		contentCardLayout = new CardLayout();// create CardLayout
												// object
		contentCardPanel.setLayout(contentCardLayout);
		contentCardPanel.add(allPhotosPanel, labels[0]); // add the photos panel
		contentCardPanel.add(albumPhotosPanel, labels[1]); // add the albums
															// panel
		contentCardPanel.add(yearPhotosPanel, labels[2]); // add the years panel

		contentCardLayout.show(contentCardPanel, labels[0]);

		return contentCardPanel;

	}

	// #################### Main Content Panels ###################

	public static JPanel allPhotoContentPanel() {
		allPhotosPanel = new JPanel();
		allPhotosPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		return allPhotosPanel;

	}

	public static JPanel albumsContentPanel() {
		JPanel albumsPanel = new JPanel();
		allPhotosPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		return albumsPanel;
	}

	public static JPanel yearsContentPanel() {
		JPanel yearsPanel = new JPanel();
		yearsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		return yearsPanel;

	}

	// #################### Bottom Details ###################

	public static JPanel bottomPhotoDetailsPanel() {

		// String nameLabelConstant = "Name: ";
		// String captionLabelConstant = "Caption: ";
		// String tagsLabelConstant = "Tags: ";

		/*
		 * JLabel photoNameLabel = new JLabel(); JLabel photoCaptionLabel = new
		 * JLabel(); JLabel photoTagsLabel = new JLabel(); JButton
		 * photosEditButton = new JButton();
		 */

		photosEditButton.setPreferredSize(new Dimension(75, 50));
		ImageIcon editIcon = new ImageIcon(containerFolder + "/EditButton.png");
		photosEditButton.setIcon(editIcon);
		photosEditButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				ActionCatchers.editPhotoClicked();
			}
		});

		CustomPanel detailsPanel = new CustomPanel();
		detailsPanel.setReversed(true);

		JPanel detailsSubPanel = new JPanel();
		// detailsPanel.add(Box.createRigidArea(new Dimension(detailsMenuHeight,
		// detailsMenuHeight)));

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		detailsPanel.setLayout(gridbag);

		GridBagLayout gridbagSub = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		detailsSubPanel.setLayout(gridbagSub);

		// detailsPanel.add(Box.createRigidArea(new Dimension(detailsMenuHeight,
		// detailsMenuHeight)));

		photoNameLabel.setText(nameLabelConstant + "Poop");
		photoCaptionLabel.setText(captionLabelConstant + "Poop");
		photoTagsLabel.setText(tagsLabelConstant + "Poop");
		photoDateLabel.setText(dateLabelConstant + "Poop");

		c.ipadx = 40;
		c.insets = new Insets(5, 50, 5, 50);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(photoNameLabel, c);
		detailsSubPanel.add(photoNameLabel, c);
		// detailsPanel.add(Box.createHorizontalStrut(100),c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		c.ipadx = 40;
		c.weightx = 1.0;
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(photoCaptionLabel, c);
		detailsSubPanel.add(photoCaptionLabel);

		c.gridx++;
		c.insets = new Insets(5, 50, 5, 0);
		c.anchor = GridBagConstraints.EAST;
		detailsSubPanel.add(photoDateLabel, c);

		c.insets = new Insets(5, 50, 5, 50);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(photoTagsLabel, c);
		// detailsPanel.add(Box.createHorizontalStrut(100),c);
		detailsSubPanel.add(photoTagsLabel, c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		con.insets = new Insets(0, 0, 0, 0);
		con.ipadx = 40;
		con.anchor = GridBagConstraints.EAST;
		con.weightx = 1.0;
		con.gridx = 2;
		con.gridy = 0;
		gridbag.setConstraints(photosEditButton, con);
		detailsPanel.add(photosEditButton, con);

		con.anchor = GridBagConstraints.WEST;
		con.weightx = 1.0;
		con.gridx = 0;
		con.gridy = 0;
		gridbag.setConstraints(detailsSubPanel, con);
		detailsPanel.add(detailsSubPanel, con);

		photoNameLabel.setVisible(false);
		photoCaptionLabel.setVisible(false);
		photoTagsLabel.setVisible(false);
		photosEditButton.setVisible(false);
		photoDateLabel.setVisible(false);

		return detailsPanel;
	}

	public static JPanel bottomAlbumDetailsPanel() {

		albumEditButton.setPreferredSize(new Dimension(115, 50));
		ImageIcon editIcon = new ImageIcon(containerFolder + "/EditButton.png");
		albumEditButton.setIcon(editIcon);
		albumEditButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				ActionCatchers.editAlbumClicked();
			}
		});

		CustomPanel detailsPanel = new CustomPanel();
		detailsPanel.setReversed(true);

		JPanel detailsSubPanel = new JPanel();
		// detailsPanel.add(Box.createRigidArea(new Dimension(detailsMenuHeight,
		// detailsMenuHeight)));

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		detailsPanel.setLayout(gridbag);

		GridBagLayout gridbagSub = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		detailsSubPanel.setLayout(gridbagSub);

		c.insets = new Insets(0, 50, 0, 50);
		c.ipadx = 40;
		// c.insets = new Insets(0, 50, 0, 50);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbagSub.setConstraints(albumNameLabel, c);
		detailsSubPanel.add(albumNameLabel, c);

		c.gridx = 3;
		gridbagSub.setConstraints(albumNumberLabel, c);
		detailsSubPanel.add(albumNumberLabel, c);

		c.insets = new Insets(0, 50, 0, 0);
		c.gridx++;
		detailsSubPanel.add(albumDatesLabel, c);

		con.insets = new Insets(0, 0, 0, 0);
		con.weightx = 1.0;
		con.gridx = 0;
		con.gridy = 0;
		con.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(detailsSubPanel, con);
		detailsPanel.add(detailsSubPanel, con);

		con.weightx = 1.0;
		con.gridx = 5;
		con.gridy = 0;
		con.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(albumEditButton, con);
		detailsPanel.add(albumEditButton, con);

		albumNameLabel.setVisible(false);
		albumNumberLabel.setVisible(false);
		albumEditButton.setVisible(false);

		return detailsPanel;
	}

	public static JPanel bottomYearsDetailsPanel() {

		CustomPanel detailsPanel = new CustomPanel();
		detailsPanel.setReversed(true);

		JPanel detailsSubPanel = new JPanel();
		// detailsPanel.add(Box.createRigidArea(new Dimension(detailsMenuHeight,
		// detailsMenuHeight)));

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		detailsPanel.setLayout(gridbag);

		GridBagLayout gridbagSub = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		detailsSubPanel.setLayout(gridbagSub);

		// detailsPanel.add(Box.createRigidArea(new Dimension(detailsMenuHeight,
		// detailsMenuHeight)));

		yearNameLabel.setText(nameLabelConstant + "Poop");
		yearCaptionLabel.setText(captionLabelConstant + "Poop");
		yearTagsLabel.setText(tagsLabelConstant + "Poop");
		yearDateLabel.setText(dateLabelConstant + "Poop");

		c.ipadx = 40;
		c.insets = new Insets(5, 50, 5, 50);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(yearNameLabel, c);
		detailsSubPanel.add(yearNameLabel, c);
		// detailsPanel.add(Box.createHorizontalStrut(100),c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		c.ipadx = 40;
		c.weightx = 1.0;
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(yearCaptionLabel, c);
		detailsSubPanel.add(yearCaptionLabel);

		c.gridx++;

		c.anchor = GridBagConstraints.EAST;
		detailsSubPanel.add(yearDateLabel, c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(yearTagsLabel, c);
		// detailsPanel.add(Box.createHorizontalStrut(100),c);
		detailsSubPanel.add(yearTagsLabel, c);

		// detailsPanel.add(Box.createHorizontalStrut(100));

		con.anchor = GridBagConstraints.WEST;
		con.weightx = 1.0;
		con.gridx = 0;
		con.gridy = 0;
		gridbag.setConstraints(detailsSubPanel, con);
		detailsPanel.add(detailsSubPanel, con);

		yearNameLabel.setVisible(false);
		yearCaptionLabel.setVisible(false);
		yearTagsLabel.setVisible(false);
		yearDateLabel.setVisible(false);

		return detailsPanel;
	}

	// ################### Create Menu ######################/
	public static JPanel createMenu() {

		icon = new UserIcon('N');

		menu = new MenuButtonsContainer(labels, menuButtonWidth,
				menuButtonHeight, null, 1);

		CustomPanel menuPanel = new CustomPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

		CustomPanel topMenuPanel = new CustomPanel();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		topMenuPanel.setLayout(gridbag);

		// topMenuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		// topMenuPanel.add(Box.createHorizontalStrut(20));

		// Quick and easy in line photo button

		ImageIcon settingsIcon = new ImageIcon(containerFolder
				+ "/settings.png");
		JLabel settingsButton = new JLabel(settingsIcon);
		settingsButton.setToolTipText("Edit settings");
		settingsButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				ActionCatchers.settingsClicked();
			}
		});

		con.insets = new Insets(0, 0, 0, 0);
		con.weightx = 1.0;
		// c.gridwidth = 5;
		con.gridx = 1;
		con.gridy = 0;
		con.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(menu, con);
		topMenuPanel.add(menu, con);

		con.insets = new Insets(0, 50, 0, 0);
		con.weightx = 1.0;
		con.gridx = 0;
		con.gridy = 0;
		con.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(settingsButton, con);
		topMenuPanel.add(settingsButton, con);

		con.insets = new Insets(0, 0, 0, 50);
		con.weightx = 1.0;
		// c.gridwidth = 5;
		con.gridx = 2;
		con.gridy = 0;
		con.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(icon, con);
		topMenuPanel.add(icon, con);
		// topMenuPanel.add(Box.createHorizontalStrut(20));

		menuPanel.add(topMenuPanel);

		// ################### Bottom Menu #####################//

		CustomPanel bottomMenuPanel = new CustomPanel();
		bottomMenuPanel.setOpacity(1.0f);

		GridBagLayout gridBagSub = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		bottomMenuPanel.setLayout(gridBagSub);

		ImageIcon backIcon = new ImageIcon(containerFolder + "/backButton.png");
		backLabel = new JLabel(backIcon);
		backLabel.setToolTipText("Go Back");
		// addLabel.setFont(new Font("Ariel", Font.PLAIN, 25));
		// addLabel.setForeground(Color.GRAY);
		backLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (backLabel.isEnabled()) {
					ActionCatchers.backClicked();
				}
			}
		});

		ImageIcon backIconDisabled = new ImageIcon(containerFolder
				+ "/backButtonDisabled.png");
		backLabel.setDisabledIcon(backIconDisabled);
		backLabel.setEnabled(false);
		// backLabel.setVisible(false);

		c.insets = new Insets(5, 25, 5, 0);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridBagSub.setConstraints(backLabel, c);
		bottomMenuPanel.add(backLabel, c);

		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		gridBagSub.setConstraints(Box.createHorizontalStrut(50), c);
		bottomMenuPanel.add(Box.createHorizontalStrut(50), c);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		ImageIcon searchIcon = new ImageIcon(containerFolder
				+ "/searchButton.png");
		searchLabel = new JLabel(searchIcon);
		searchLabel.setEnabled(false);

		ImageIcon searchIconDisabled = new ImageIcon(containerFolder
				+ "/searchButtonDisabled.png");
		searchLabel.setDisabledIcon(searchIconDisabled);

		searchLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {

				if (searchLabel.isEnabled()) {
					ActionCatchers.searchClicked();
				}
			}
		});

		// searchLabel.setEnabled(false);

		search = new HintTextField("search by tags");
		search.setPreferredSize(new Dimension(200, 30));
		search.setToolTipText("Example: \"car:white, car:bmw\"");
		search.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				// System.out.println("hello1");
				if (search.getText().equals("")) {
					searchLabel.setEnabled(false);
				} else {
					searchLabel.setEnabled(true);
				}
			}

			public void removeUpdate(DocumentEvent e) {
				if (search.getText().equals("")) {
					searchLabel.setEnabled(false);
				} else {
					searchLabel.setEnabled(true);
				}
			}

			public void changedUpdate(DocumentEvent e) {
				// System.out.println("hello3");
			}
		});

		searchPanel.add(search);
		searchPanel.add(Box.createHorizontalStrut(4));
		searchPanel.add(searchLabel);

		c.insets = new Insets(5, 0, 5, 0);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 3;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		gridBagSub.setConstraints(searchPanel, c);
		bottomMenuPanel.add(searchPanel, c);

		slider = new JSlider(JSlider.HORIZONTAL, 35, 100, initScale);
		slider.setPreferredSize(new Dimension(125, 20));
		slider.setToolTipText("Change the size of the thumbnails");
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();

				// System.out.println("Value : " + source.getValue());
				if (!source.getValueIsAdjusting()) {

					// Which tab is selected on top
					switch (menu.whichTabbed()) {

					case (0):
						allPhotosCollection.setScale((float) ((JSlider) e
								.getSource()).getValue() / 100);
						allPhotosCollection.repaint();
						break;
					case (1):
						if (singleAlbum) {
							singleAlbumCollection.setScale((float) ((JSlider) e
									.getSource()).getValue() / 100);
							singleAlbumCollection.repaint();

						} else {

							albumsCollection.setScale((float) ((JSlider) e
									.getSource()).getValue() / 100);
							albumsCollection.repaint();
						}
						break;
					case (2):

						yearsCollection.setScale(.35f);
						yearsCollection.repaint();
						break;

					}

				}

			}
		});

		JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));

		ImageIcon thumbSmallIcon = new ImageIcon(containerFolder
				+ "/thumbSmall.png");
		JLabel thumbSmall = new JLabel(thumbSmallIcon);

		ImageIcon thumbBigIcon = new ImageIcon(containerFolder
				+ "/thumbBig.png");
		JLabel thumbBig = new JLabel(thumbBigIcon);

		sliderPanel.add(thumbSmall);
		sliderPanel.add(slider);
		sliderPanel.add(thumbBig);

		c.insets = new Insets(5, 0, 5, 0);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 4;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		gridBagSub.setConstraints(sliderPanel, c);
		bottomMenuPanel.add(sliderPanel, c);

		JPanel addDeletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,
				0));
		ImageIcon addIcon = new ImageIcon(containerFolder + "/addButton.png");
		ImageIcon addIconDisabled = new ImageIcon(containerFolder
				+ "/addButtonDisabled.png");
		JLabel addLabel = new JLabel(addIcon);
		addLabel.setDisabledIcon(addIconDisabled);
		addLabel.setToolTipText("Add a photo");
		addLabel.setFont(new Font("Ariel", Font.PLAIN, 25));
		addLabel.setForeground(Color.GRAY);
		addLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (addLabel.isEnabled()) {
					ActionCatchers.addClicked();
				}
			}
		});

		ImageIcon deleteIcon = new ImageIcon(containerFolder
				+ "/deleteButton.png");
		ImageIcon deleteIconDisabled = new ImageIcon(containerFolder
				+ "/deleteButtonDisabled.png");
		JLabel deleteLabel = new JLabel(deleteIcon);
		deleteLabel.setDisabledIcon(deleteIconDisabled);
		deleteLabel.setToolTipText("Delete a photo");
		deleteLabel.setFont(new Font("Ariel", Font.PLAIN, 25));
		deleteLabel.setForeground(Color.GRAY);
		deleteLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (deleteLabel.isEnabled()) {
					ActionCatchers.deleteClicked();
				}
			}
		});

		addDeletePanel.add(deleteLabel);
		addDeletePanel.add(Box.createHorizontalStrut(4));
		addDeletePanel.add(addLabel);

		c.insets = new Insets(5, 0, 5, 25);
		c.weightx = 1.0;
		// c.gridwidth = 5;
		c.gridx = 5;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		gridBagSub.setConstraints(addDeletePanel, c);
		bottomMenuPanel.add(addDeletePanel, c);

		menuPanel.add(bottomMenuPanel);

		// ############### Menu Tab Changes ####################//
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				menu.requestFocus();
				MenuButton sender = (MenuButton) evt.getSource();
				// CardLayout cardLayout = (CardLayout)
				// contentCardLayout.getLayout();

				if (searching == true) {

					allPhotosPanel.removeAll();
					allPhotosPanel.add(allPhotosCollection);
					allPhotosPanel.repaint();

					albumPhotosPanel.removeAll();
					albumPhotosPanel.add(albumsCollection);
					albumPhotosPanel.repaint();

					yearPhotosPanel.removeAll();
					yearPhotosPanel.add(yearsCollection);
					yearPhotosPanel.repaint();

				}

				BorderLayout layout = (BorderLayout) mainContent.getLayout();
				mainContent.remove(layout
						.getLayoutComponent(BorderLayout.CENTER));

				mainContent.add(contentCardPanel, BorderLayout.CENTER);

				contentCardLayout.show(contentCardPanel, sender.getLabel());
				bottomDetailLayout.show(bottomDetailPanel, sender.getLabel());

				search.setText("");
				((HintTextField) search).focusLost(null);

				switch (menu.whichTabbed()) {

				case (0):

					if (allPhotosCollection.getSelection() < 0) {
						// set the labels and stuff
						photoNameLabel.setVisible(false);
						photoCaptionLabel.setVisible(false);
						photoTagsLabel.setVisible(false);
						photosEditButton.setVisible(false);
						photoDateLabel.setVisible(false);

						photoNameLabel.setText("");
						photoCaptionLabel.setText("");
						photoTagsLabel.setText("");
						photoDateLabel.setText("");

					} else {

						photo = new PhotoEditorController(user
								.getPhotoByIndex(allPhotosCollection
										.getSelection()), null);

						photoNameLabel.setText(nameLabelConstant
								+ photo.getPhotoFilename());
						photoCaptionLabel.setText(captionLabelConstant
								+ photo.getPhotoCaption());
						photoTagsLabel.setText(tagsLabelConstant
								+ photo.getTagsString());
						photoDateLabel.setText(dateLabelConstant
								+ photo.getPhotoDate());

					}

					search.setToolTipText("Example: \"car:white, car:bmw\"");
					search.setText("search by tags");
					break;
				case (1):
					if (singleAlbum) {
						search.setText("search " + album.getName());

					} else {
						search.setText("search album");

					}
					search.setToolTipText("Example: \"car:white, car:bmw\"");
					break;
				case (2):
					search.setText("search by date");
					// yearNameLabel.setText(nameLabelConstant + "Poop");
					// yearCaptionLabel.setText(captionLabelConstant + "Poop");
					// yearTagsLabel.setText(tagsLabelConstant + "Poop");
					// yearDateLabel.setText(dateLabelConstant + "Poop");

					search.setToolTipText("Example: \"6/5/1993-7:56:37 4/15/2014-13:32:28\"");

					break;

				}
				searchLabel.setEnabled(false);

				searching = false;

				// Switch slider position to keep it independent and all
				switch (menu.whichTabbed()) {
				case (0):

					slider.setEnabled(true);
					addLabel.setEnabled(true);
					deleteLabel.setEnabled(true);

					slider.setValue((int) (allPhotosCollection.getScale() * 100));
					backLabel.setEnabled(false);
					break;
				case (1):

					slider.setEnabled(true);
					addLabel.setEnabled(true);
					deleteLabel.setEnabled(true);

					if (singleAlbum) {
						backLabel.setEnabled(true);
						slider.setValue((int) (singleAlbumCollection.getScale() * 100));

					} else {
						backLabel.setEnabled(false);
						slider.setValue((int) (albumsCollection.getScale() * 100));
					}
					break;
				case (2):

					
					slider.setValue((int) (yearsCollection.getScale() * 100));
					slider.setEnabled(false);
					addLabel.setEnabled(false);
					deleteLabel.setEnabled(false);
					backLabel.setEnabled(false);
					// slider.setValue(albumsCollection.getScale());
					break;

				}
			}
		});

		// System.out.println(bottomMenuPanel.getBounds());

		return menuPanel;
	}

	public static JPanel createGlassPane(JFrame f) {
		JPanel glass = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				g.setColor(new Color(0, 0, 0, 140));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		glass.setOpaque(false);
		glass.setLayout(new GridBagLayout());
		f.setGlassPane(glass);
		glass.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				me.consume();
			}
		});
		// glass.setVisible(true);
		return glass;
	}

	protected static void logout() {

		backend.save();
		user = null;
		album = null;
		photo = null;

		singleAlbum = false;
		searching = false;

		frame.dispose();

		frame = buildFrame();

		frame.setVisible(true);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				backend.save();
			}
		}));

		buildLoginWindow();

	}

	protected static void sliderMoved() {

		// may not use this because it needs the source event

	}

	static boolean preLoginLoad() {

		// user = new UserEditorController(backend.loginUser(userString));

		if (user != null) {
			gui = new GuiView();
			return true;
		}

		return false;
	}

	@Override
	public void update(Observable o, Object arg) {

		// This Captures the events from the collectionView

		// could change this to an int later on to capture more actions here
		boolean doubleClicked = (boolean) arg;

		if (doubleClicked) {

			ActionCatchers.photoDoubleClicked();
			System.out.println("double clicked");
		} else {

			ActionCatchers.photoSingleClicked();

		}

	}

	static void createSingleAlbumView(boolean reuse) {

		// System.out.println("here");
		if (reuse == false) {
			album = new AlbumEditorController(
					user.getAlbumByIndex(albumsCollection.getSelection()),
					user.getModel());
		}

		int numPhotos = album.getAlbumSize();

		File[] thumbnailFilePaths = album.getThumbnails();

		BufferedImage photos[] = new BufferedImage[numPhotos];

		try {

			for (int i = 0; i < numPhotos; i++) {
				photos[i] = ImageIO.read(thumbnailFilePaths[i]);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		singleAlbumCollection = buildPhotoCollection(photos, album.getName(),
				null, gui);

		albumPhotosPanel.removeAll();
		albumPhotosPanel.add(singleAlbumCollection);
		albumPhotosPanel.repaint();
	}

	public static void refreshPhotoCollections() {
		BuildInitCollections(gui, true);

		allPhotosPanel.removeAll();
		allPhotosPanel.add(allPhotosCollection);
		allPhotosPanel.repaint();

		albumPhotosPanel.removeAll();
		albumPhotosPanel.add(albumsCollection);
		albumPhotosPanel.repaint();

		yearPhotosPanel.removeAll();
		yearPhotosPanel.add(yearsCollection);
		yearPhotosPanel.repaint();

		if (singleAlbum == true) {

			albumPhotosPanel.removeAll();
			createSingleAlbumView(true);
			albumPhotosPanel.repaint();
		}

		photoNameLabel.setText("");
		photoCaptionLabel.setText("");
		photoTagsLabel.setText("");
		photosEditButton.setText("");
		// Albums
		albumNameLabel.setText("");
		albumCaptionLabel.setText("");
		albumNumberLabel.setText("");
		albumEditButton.setText("");
		// Years
		yearNameLabel.setText("");
		yearCaptionLabel.setText("");
		yearTagsLabel.setText("");
		yearDateLabel.setText("");

		// Search
		searchNameLabel.setText("");
		searchCaptionLabel.setText("");
		searchTagsLabel.setText("");
		searchDateLabel.setText("");
		searchMakeAlbumButton.setText("");

	}

	static void createPhotoViewer(File[] paths, int index) {

		viewing = true;

		GuiView.glass.setVisible(true);
		glass.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (viewing) {
					photo_viewer_window.dispose();
					glass.setVisible(false);
					viewing = false;
				}
			}
		});
		photo_viewer_window = new JWindow(GuiView.frame);
		// photo_viewer_window.setAlwaysOnTop(true);
		photo_viewer_window.setPreferredSize(new Dimension(800, 600));

		photo_viewer_window.setLayout(new BorderLayout(0, 0));
		photo_viewer_window.add(new PhotoViewer(paths, index, 800, 600));
		photo_viewer_window.pack();
		photo_viewer_window.setLocationRelativeTo(GuiView.frame);
		photo_viewer_window.setVisible(true);
	}

	public static void badInput(JTextField box) {
		box.setBorder(new RoundedCornerBorder(Color.RED));
	}

	public static void goodInput(JTextField box) {
		box.setBorder(new RoundedCornerBorder(new Color(130, 240, 130)));
	}

	public static void save() {
		backend.save();
	}

	private static void buildLoginWindow() {

		JTextField name = new HintTextField("username");
		name.setPreferredSize(new Dimension(150, 25));

		login_window = new JWindow(GuiView.frame);
		login_window.setAlwaysOnTop(true);
		login_window.setPreferredSize(new Dimension(300, 175));
		login_window.setLayout(new BoxLayout(login_window.getContentPane(),
				BoxLayout.PAGE_AXIS));

		JPanel add_window_grid = new JPanel();
		add_window_grid.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel title = new JLabel("Login");
		title.setFont(new Font("Arial", Font.PLAIN, 25));
		title.setForeground(Color.GRAY);

		// Add title
		gbc.insets = new Insets(10, 10, 0, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		add_window_grid.add(Box.createRigidArea(new Dimension(40, 40)), gbc);

		add_window_grid.add(title, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		add_window_grid.add(new JLabel("Username: "), gbc);

		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		add_window_grid.add(name, gbc);

		login_window.add(add_window_grid);
		login_window.add(Box.createVerticalStrut(20));

		// Create and customize the confirm/cancel buttons
		JLabel login = new JLabel("login", SwingConstants.CENTER);

		login.setFont(new Font("Arial", Font.PLAIN, 25));

		login.setBackground(new Color(108, 153, 231));
		login.setForeground(Color.DARK_GRAY);
		login.setOpaque(true);

		JPanel cancel_confirm_Outer = new JPanel(new GridLayout());

		cancel_confirm_Outer.add(login);

		login.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {

				if (!name.getText().equals("")) {
					if (ActionCatchers.loginClicked(name.getText().trim())) {

						login_window.dispose();
						glass.setVisible(false);
					} else {

						badInput(name);

					}

				} else {
					badInput(name);
				}
			}
		});

		login_window.add(cancel_confirm_Outer);
		// login_window.add(Box.createVerticalStrut(10));
		// Don't change!

		login_window.pack();
		login_window.setLocationRelativeTo(GuiView.frame);
		login_window.setVisible(true);
		// GuiView.glass.requestFocus();

	}
}
