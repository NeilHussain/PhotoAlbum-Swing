package cs213.photoAlbum.guiview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import cs213.photoAlbum.control.AlbumEditorController;
import cs213.photoAlbum.control.PhotoEditorController;
import cs213.photoAlbum.control.UserEditorController;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.CollectionListener;
import cs213.photoAlbum.util.CollectionView;
import cs213.photoAlbum.util.HintTextField;
import cs213.photoAlbum.util.ImageUtilities;

public class ActionCatchers {

	static JTextArea chosenFile;

	/**
	 * Pops up a add photo window
	 * 
	 * @param optional
	 *            album name of the new photo
	 */
	// ################### Add Photo ##########################//
	public static void addPhoto(String aName) {

		if (GuiView.user.getAlbumNames().length == 0) {
			// Add album first
			addAlbum();
			return;
		}

		GuiView.glass.setVisible(true);

		GuiView.albumName = aName;

		JTextField name = new HintTextField("");
		JTextField caption = new HintTextField("");
		// JTextField albumTF = new HintTextField("");

		name.setPreferredSize(new Dimension(150, 25));
		caption.setPreferredSize(new Dimension(150, 25));
		// albumTF.setPreferredSize(new Dimension(150, 25));

		JLabel cancel = new JLabel("cancel", SwingConstants.CENTER);
		JLabel confirm = new JLabel("confirm", SwingConstants.CENTER);
		confirm.setEnabled(false);

		GuiView.addPhoto_window = new JWindow(GuiView.frame);
		GuiView.addPhoto_window.setLayout(new BoxLayout(GuiView.addPhoto_window
				.getContentPane(), BoxLayout.PAGE_AXIS));

		GuiView.addPhoto_window.setPreferredSize(new Dimension(350, 450));

		JPanel add_window_grid = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel title = new JLabel("Add Photo");
		title.setFont(new Font("Arial", Font.PLAIN, 25));
		title.setForeground(Color.GRAY);

		// title.setBackground(Color.BLUE);
		// title.setOpaque(true);

		// Add title
		gbc.insets = new Insets(10, 10, 0, 10);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		// add_window_grid.add(Box.createRigidArea(new Dimension(40, 40)), gbc);
		add_window_grid.add(title, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		add_window_grid.add(new JLabel("Name: "), gbc);
		gbc.gridy++;
		add_window_grid.add(new JLabel("Caption: "), gbc);

		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		add_window_grid.add(name, gbc);
		gbc.gridy++;
		add_window_grid.add(caption, gbc);

		if (GuiView.albumName == null) {
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.LINE_END;
			// add_window_grid.add(new JLabel("Album: "), gbc);
			add_window_grid.add(new JLabel("Album: "), gbc);

			gbc.gridy = 3;
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.CENTER;
			// add_window_grid.add(albumTF, gbc);

			GuiView.aList = new JList<String>(GuiView.user.getAlbumNames());
			GuiView.aList.setSelectedIndex(0);
			// System.out.println(GuiView.aList.getSelectedValue());

		} else {

			gbc.gridy = 3;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.LINE_END;
			add_window_grid.add(new JLabel("Album: "), gbc);

			gbc.gridy = 3;
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.LINE_START;
			add_window_grid.add(new JLabel(GuiView.albumName), gbc);

			GuiView.addPhoto_window.setPreferredSize(new Dimension(350, 350));

		}

		GuiView.addPhoto_window.add(add_window_grid);

		if (GuiView.albumName == null) {
			// GuiView.addPhoto_window.add(new JLabel("Album"));
		}

		// Add album list after the gridbag if there is no selected album
		if (GuiView.albumName == null) {
			// GuiView.addPhoto_window.add(Box.createVerticalStrut(10));
			GuiView.addPhoto_window.add(new JScrollPane(GuiView.aList), gbc);
		}

		GuiView.addPhoto_window.add(Box.createVerticalStrut(10));
		JLabel fileChooser = new JLabel("Choose File", SwingConstants.CENTER);
		fileChooser.setFont(new Font("Arial", Font.PLAIN, 18));
		fileChooser.setBackground(new Color(108, 153, 231));
		fileChooser.setForeground(Color.WHITE);
		fileChooser.setOpaque(true);

		JPanel chooser = new JPanel(new GridLayout());
		chooser.add(fileChooser);

		GuiView.addPhoto_window.add(chooser);

		chosenFile = new JTextArea("No file selected.");
		// chosenFile.setPreferredSize(new
		// Dimension(GuiView.addPhoto_window.getWidth(), 20));
		// chosenFile.setMinimumSize(new
		// Dimension(GuiView.addPhoto_window.getWidth(), 20));
		chosenFile.setEditable(false);
		chosenFile.setLineWrap(true);
		GuiView.addPhoto_window.add(chosenFile);

		fileChooser.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				JFrame parent = new JFrame();
				FileDialog fd = new FileDialog(parent, "Please choose a file:",
						FileDialog.LOAD);
				fd.setVisible(true);
				String filePath = fd.getDirectory();
				String fileName = fd.getFile();
				// System.out.println("path: " + filePath + "\nname: " +
				// fileName
				// + "\n");
				if (filePath == null || fileName == null) {
					confirm.setEnabled(false);
					chosenFile.setText("No file selected.");
				} else {
					GuiView.file = fileName;
					GuiView.directoryPath = filePath;
					confirm.setEnabled(true);
					chosenFile.setText(filePath + fileName);
				}
				parent.dispose();
			}
		});

		GuiView.addPhoto_window.add(Box.createVerticalStrut(10));

		// Create and customize the confirm/cancel buttons

		cancel.setFont(new Font("Arial", Font.PLAIN, 25));
		confirm.setFont(new Font("Arial", Font.PLAIN, 25));

		cancel.setBackground(new Color(240, 130, 130));
		cancel.setForeground(Color.DARK_GRAY);
		cancel.setOpaque(true);

		confirm.setBackground(new Color(130, 240, 130));
		confirm.setForeground(Color.DARK_GRAY);
		confirm.setOpaque(true);

		JPanel cancel_confirm_Outer = new JPanel(new GridLayout());
		cancel_confirm_Outer.add(cancel);
		cancel_confirm_Outer.add(confirm);

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				GuiView.addPhoto_window.dispose();
				GuiView.glass.setVisible(false);

			}
		});

		confirm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (confirm.isEnabled()) {
					// Assumes inputs are all fine and dandy...

					/*
					 * if (!albumTF.getText().equals("") && aName == null) {
					 * 
					 * GuiView.album =
					 * GuiView.user.editAlbum(GuiView.albumName);
					 * 
					 * }
					 */

					if (GuiView.albumName == null) {
						// System.out.println("\""
						// + GuiView.aList.getSelectedValue() + "\"");
						GuiView.albumName = GuiView.aList.getSelectedValue()
								.toString();
						GuiView.album = GuiView.user
								.editAlbum(GuiView.albumName);
					}

					if (GuiView.album != null) {
						GuiView.album = GuiView.user
								.editAlbum(GuiView.albumName);
						String userDir = System.getProperty("user.dir")
								+ File.separator;

						// System.out.println("User.dir: " + userDir);
						// System.out.println("File path: " + directoryPath);

						int error = 0;
						if (userDir.equals((GuiView.directoryPath))) {
							// photo is from working path, just store file name
							error = GuiView.album.addAlbumPhoto(name.getText(),
									GuiView.file, caption.getText());

						} else {
							// photo is from some outside path. Should store
							// full
							// canocial path
							error = GuiView.album.addAlbumPhoto(name.getText(),
									GuiView.directoryPath + GuiView.file,
									caption.getText());
						}

						// caption.getText());

						if (error == 0) {

							// System.out.println("File " + GuiView.file
							// + " does not exist");

						} else if (error == -1) {

							/*
							 * System.out.println("Photo " + GuiView.file +
							 * " already exists in album " + GuiView.albumName);
							 */

						} else if (error == 1) {

							GuiView.save();

							GuiView.refreshPhotoCollections();

							/*
							 * System.out.println("Added photo " + GuiView.file
							 * + ":\n" + caption.getText() + " - Album: " +
							 * GuiView.albumName);
							 */

							GuiView.albumName = "";
							GuiView.directoryPath = "";
							GuiView.file = "";

							GuiView.addPhoto_window.dispose();
							GuiView.glass.setVisible(false);

							// album.addAlbumPhoto(file, caption.getText());
						}

					} else {

						// GuiView.badInput(albumTF);
						// albumTF.setText("");
						GuiView.albumName = "";

						// System.out.println("Album " + GuiView.albumName
						// + " does not exist");

					}
				}
			}
		});

		GuiView.addPhoto_window.add(cancel_confirm_Outer);

		GuiView.addPhoto_window.pack();
		GuiView.addPhoto_window.setLocationRelativeTo(GuiView.frame);
		GuiView.addPhoto_window.setVisible(true);

	}

	// ################### Add Album ##########################//
	/**
	 * Pops up an add album window
	 */
	static void addAlbum() {
		GuiView.glass.setVisible(true);

		JTextField name = new JTextField(12);
		JTextField caption = new JTextField(12);

		GuiView.addAlbum_window = new JWindow(GuiView.frame);
		GuiView.addAlbum_window.setLayout(new BoxLayout(GuiView.addAlbum_window
				.getContentPane(), BoxLayout.PAGE_AXIS));

		JPanel add_window_grid = new JPanel();
		add_window_grid.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel title = new JLabel("Add Album");
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
		add_window_grid.add(new JLabel("Name: "), gbc);
		gbc.gridy++;
		add_window_grid.add(new JLabel("Caption: "), gbc);

		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		add_window_grid.add(name, gbc);
		gbc.gridy++;
		add_window_grid.add(caption, gbc);

		GuiView.addAlbum_window.add(add_window_grid);
		GuiView.addAlbum_window.add(Box.createVerticalStrut(20));

		// Create and customize the confirm/cancel buttons

		JLabel cancel = new JLabel("cancel", SwingConstants.CENTER);
		JLabel confirm = new JLabel("confirm", SwingConstants.CENTER);
		cancel.setFont(new Font("Arial", Font.PLAIN, 25));
		confirm.setFont(new Font("Arial", Font.PLAIN, 25));

		cancel.setBackground(new Color(240, 130, 130));
		cancel.setForeground(Color.DARK_GRAY);
		cancel.setOpaque(true);

		confirm.setBackground(new Color(130, 240, 130));
		confirm.setForeground(Color.DARK_GRAY);
		confirm.setOpaque(true);

		JPanel cancel_confirm_Outer = new JPanel(new GridLayout());
		cancel_confirm_Outer.add(cancel);
		cancel_confirm_Outer.add(confirm);

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				GuiView.addAlbum_window.dispose();
				GuiView.glass.setVisible(false);

			}
		});

		confirm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				// System.out.println("adding");
				if (!name.getText().equals("")) {

					if (GuiView.user.addUserAlbum(name.getText().trim())) {

						if (!caption.getText().equals("")) {
							GuiView.user.getAlbum(name.getText()).setCaption(
									caption.getText());
						}

						// albumCreation worked

						if (GuiView.searching == true) {

							// System.out.println("Have to make an album here");
							GuiView.album = new AlbumEditorController(
									GuiView.user
											.getAlbum(name.getText().trim()),
									null);

							for (Photo photo : GuiView.searchResults) {
								GuiView.album.addPhoto(photo);
							}

						}

						GuiView.addAlbum_window.dispose();
						GuiView.glass.setVisible(false);

						// buildAlbumPhotoCollection();
						GuiView.refreshPhotoCollections();

						if (GuiView.searching == true) {
							GuiView.searching = false;
							// set it to the albums view
							GuiView.menu.setTabbed(1);
							GuiView.contentCardLayout
									.show(GuiView.contentCardPanel,
											GuiView.labels[1]);
						}

					} else {

						GuiView.badInput(name);
					}

				}
			}
		});

		GuiView.addAlbum_window.add(cancel_confirm_Outer);

		// Don't change!

		GuiView.addAlbum_window.pack();
		GuiView.addAlbum_window.setLocationRelativeTo(GuiView.frame);
		GuiView.addAlbum_window.setVisible(true);

	}

	// ################### Add Button Clicked ##########################//
	protected static void addClicked() {
		// System.out.println("Add clicked");
		// Which tab is selected on top: photos(0), albums(0), years(0)
		switch (GuiView.menu.whichTabbed()) {

		case (0):
			addPhoto(null);
			break;
		case (1):

			if (GuiView.singleAlbum == true) {

				ActionCatchers.addPhoto(GuiView.album.getName());
			} else {
				addAlbum();

			}
			break;
		case (2):
			break;

		}

		// addPhoto();

	}

	// ################### Settings ##########################//
	protected static void settingsClicked() {
		GuiView.glass.setVisible(true);

		JTextField name = new HintTextField(GuiView.user.getFullName());
		name.setPreferredSize(new Dimension(200, 25));
		// name.setText(user.getFullName());

		GuiView.settings_window = new JWindow(GuiView.frame);
		GuiView.settings_window.setLayout(new BoxLayout(GuiView.settings_window
				.getContentPane(), BoxLayout.PAGE_AXIS));

		JPanel add_window_grid = new JPanel();
		add_window_grid.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel title = new JLabel("User Settings");
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
		add_window_grid.add(new JLabel("New name: "), gbc);

		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		add_window_grid.add(name, gbc);

		GuiView.settings_window.add(add_window_grid);
		GuiView.settings_window.add(Box.createVerticalStrut(20));

		// Create and customize the confirm/cancel buttons

		JLabel cancel = new JLabel("cancel", SwingConstants.CENTER);
		JLabel confirm = new JLabel("confirm", SwingConstants.CENTER);
		JLabel logout = new JLabel("logout", SwingConstants.CENTER);

		logout.setFont(new Font("Arial", Font.PLAIN, 25));
		cancel.setFont(new Font("Arial", Font.PLAIN, 25));
		confirm.setFont(new Font("Arial", Font.PLAIN, 25));

		logout.setBackground(new Color(108, 153, 231));
		logout.setForeground(Color.DARK_GRAY);
		logout.setOpaque(true);

		cancel.setBackground(new Color(240, 130, 130));
		cancel.setForeground(Color.DARK_GRAY);
		cancel.setOpaque(true);

		confirm.setBackground(new Color(130, 240, 130));
		confirm.setForeground(Color.DARK_GRAY);
		confirm.setOpaque(true);

		JPanel cancel_confirm_Outer = new JPanel(new GridLayout());
		cancel_confirm_Outer.add(cancel);
		cancel_confirm_Outer.add(logout);
		cancel_confirm_Outer.add(confirm);

		logout.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				GuiView.logout();
				GuiView.settings_window.dispose();
				GuiView.glass.setVisible(false);

			}
		});

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				GuiView.settings_window.dispose();
				GuiView.glass.setVisible(false);

			}
		});

		confirm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (!name.getText().equals("")) {
					GuiView.user.setFullName(name.getText());
					GuiView.icon.setInitial(GuiView.user.getFullName()
							.toUpperCase().charAt(0));
					GuiView.icon.repaint();
					GuiView.settings_window.dispose();
					GuiView.glass.setVisible(false);

					GuiView.save();

				} else {
					GuiView.badInput(name);
				}
			}
		});

		GuiView.settings_window.add(cancel_confirm_Outer);

		// Don't change!

		GuiView.settings_window.pack();
		GuiView.settings_window.setLocationRelativeTo(GuiView.frame);
		GuiView.settings_window.setVisible(true);
		GuiView.glass.requestFocus();
	}

	// ################### Search Clicked ##########################//
	protected static void searchClicked() {

		// operating on user;

		// int numPhotos = user.numPhotos();
		// File[] thumbnailFilePaths = user.getAllThumbnails();
		// BufferedImage photos[] = new BufferedImage[numPhotos];

		int numPhotos = 0;
		File[] thumbnailFilePaths = null;
		BufferedImage photos[] = null;
		boolean flag = true;

		switch (GuiView.menu.whichTabbed()) {

		case (0): {
			// search by tags, all photos
			//
			//
			String tags[] = GuiView.search.getText().split(", ");

			GuiView.searchResults = GuiView.user.getPhotoObjsByTags(tags);

			numPhotos = GuiView.searchResults.length;

			break;
		}
		case (1): {

			if (GuiView.singleAlbum) {
				// search per album
				//
				//
				// System.out.println(GuiView.singleAlbum);

				String tags[] = GuiView.search.getText().split(", ");

				GuiView.searchResults = GuiView.album.getPhotoObjsByTags(tags);

				numPhotos = GuiView.searchResults.length;

			} else {
				// search albums
				//
				//
				String tags[] = GuiView.search.getText().split(", ");

				GuiView.searchResults = GuiView.user.getPhotoObjsByTags(tags);

				numPhotos = GuiView.searchResults.length;

			}
			break;
		}
		case (2):
			// search by date
			//
			//
			String dates[] = GuiView.search.getText().split(" ");
			Date begin = null;
			Date end = null;

			if (dates.length == 2) {
				try {
					begin = GuiView.sdf.parse(dates[0]);
				} catch (ParseException e) {
					// reportError("Date not formatted correctly");
					flag = false;
				}

				try {
					end = GuiView.sdf.parse(dates[1]);
				} catch (ParseException e) {
					flag = false;
					// reportError("Date not formatted correctly");
				}
			} else {
				flag = false;
			}

			if (flag) {
				GuiView.searchResults = GuiView.user.getPhotosObjsByDate(begin,
						end);
				numPhotos = GuiView.searchResults.length;
				flag = true;
			}

			break;

		}

		if (numPhotos > 0) {

			GuiView.bottomDetailLayout
					.show(GuiView.bottomDetailPanel, "search");
			GuiView.searching = true;
			GuiView.goodInput(GuiView.search);

			thumbnailFilePaths = GuiView.user
					.getThumbnails(GuiView.searchResults);
			photos = new BufferedImage[numPhotos];

			try {

				for (int i = 0; i < numPhotos; i++) {
					photos[i] = ImageIO.read(thumbnailFilePaths[i]);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// This should mostly be the same (except thumbnails don't need to
			// be
			// generated again, just loaded from the folder)

			try {
				GuiView.searchCollection = new CollectionView(
						ImageUtilities.getMultipleThumbnails(photos,
								GuiView.thumbWidth, GuiView.thumbHeight),
						GuiView.thumbWidth, GuiView.thumbHeight,
						GuiView.frameWidth, GuiView.frameHeight);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GuiView.searchCollection.setSize(GuiView.frameWidth,
					GuiView.frameHeight);
			GuiView.searchCollection.setTag("search");
			// collection.setSpacing(2);

			CollectionListener cl = new CollectionListener(
					GuiView.searchCollection);
			cl.addObserver(GuiView.gui);
			GuiView.searchCollection.addMouseListener(cl);
			GuiView.searchCollection.addMouseMotionListener(cl);
			GuiView.searchCollection.addMouseWheelListener(cl);

			GuiView.searchCollection.setScale((float) GuiView.initScale / 100);

			// Adding the photos collection
			switch (GuiView.menu.whichTabbed()) {
			case (0):
				GuiView.allPhotosPanel.removeAll();
				GuiView.allPhotosPanel.add(GuiView.searchCollection);
				GuiView.allPhotosPanel.repaint();

				break;
			case (1):

				if (GuiView.singleAlbum == true) {
					GuiView.singleAlbum = false;
					GuiView.slider.setValue((int) (GuiView.searchCollection
							.getScale() * 100));
					GuiView.albumPhotosPanel.removeAll();
					GuiView.albumPhotosPanel.add(GuiView.searchCollection);
					GuiView.albumPhotosPanel.repaint();
				} else {
					// searching is true.
					GuiView.slider.setValue((int) (GuiView.searchCollection
							.getScale() * 100));
					GuiView.albumPhotosPanel.removeAll();
					GuiView.albumPhotosPanel.add(GuiView.searchCollection);
					GuiView.albumPhotosPanel.repaint();
				}

				break;
			case (2):

				GuiView.yearPhotosPanel.removeAll();
				GuiView.yearPhotosPanel.add(GuiView.searchCollection);
				GuiView.yearPhotosPanel.repaint();
				break;

			}

			GuiView.backLabel.setEnabled(true);
			GuiView.singleAlbum = false;
		} else {

			GuiView.badInput(GuiView.search);

		}
	}

	// ################### Login Clicked ##########################//
	protected static boolean loginClicked(String stringUser) {

		// Temp
		// load up the images/albums/years content panel and all that good stuff
		// this might be hard to do with the project in the current state, will
		// work on it

		//
		//
		//

		if (stringUser.equalsIgnoreCase("admin")) {
			GuiView.mainCardLayout.show(GuiView.mainCardPanel, "admin");
			GuiView.admin_window.setVisible(true);
			GuiView.login_window.setVisible(false);
			return true;
		}

		User logged = GuiView.backend.loginUser(stringUser);

		if (logged != null) {

			// login user
			GuiView.user = new UserEditorController(logged);

			// load some stuff before they are logged in (Loading screen? :O )
			GuiView.preLoginLoad();

			GuiView.mainCardLayout.show(GuiView.mainCardPanel, "main");

			GuiView.icon.setInitial(GuiView.user.getFullName().toUpperCase()
					.charAt(0));

			return true;

		}

		return false;

	}

	// ################### Back Clicked ##########################//
	protected static void backClicked() {
		// System.out.println("back clicked");

		// System.out.println("Searched clicked");
		// Which tab is selected on top: photos(0), albums(0), years(0)

		switch (GuiView.menu.whichTabbed()) {

		case (0):
			if (GuiView.searching == true) {
				GuiView.slider.setValue((int) (GuiView.allPhotosCollection
						.getScale() * 100));
				GuiView.allPhotosPanel.removeAll();
				GuiView.allPhotosPanel.add(GuiView.allPhotosCollection);
				GuiView.allPhotosPanel.repaint();
				GuiView.backLabel.setEnabled(false);
				GuiView.bottomDetailLayout.show(GuiView.bottomDetailPanel,
						"photos");
			}
			break;
		case (1):

			if (GuiView.searching == true) {
				// reset to default albumsCollection
				GuiView.singleAlbum = false;
				GuiView.slider.setValue((int) (GuiView.albumsCollection
						.getScale() * 100));
				GuiView.albumPhotosPanel.removeAll();
				GuiView.albumPhotosPanel.add(GuiView.albumsCollection);
				GuiView.albumPhotosPanel.repaint();
				GuiView.bottomDetailLayout.show(GuiView.bottomDetailPanel,
						"albums");
			}

			if (GuiView.singleAlbum == true) {
				GuiView.singleAlbum = false;
				GuiView.bottomDetailLayout.show(GuiView.bottomDetailPanel,
						"albums");
				GuiView.slider.setValue((int) (GuiView.albumsCollection
						.getScale() * 100));
				GuiView.albumPhotosPanel.removeAll();
				GuiView.albumPhotosPanel.add(GuiView.albumsCollection);
				GuiView.albumEditButton.setVisible(true);

				GuiView.albumPhotosPanel.repaint();
			}
			// searching is true.

			break;
		case (2):

			if (GuiView.searching == true) {
				GuiView.yearPhotosPanel.removeAll();
				GuiView.yearPhotosPanel.add(GuiView.yearsCollection);
				GuiView.yearPhotosPanel.repaint();
				GuiView.backLabel.setEnabled(false);
				GuiView.bottomDetailLayout.show(GuiView.bottomDetailPanel,
						"years");
				break;
			}

		}

		GuiView.search.setText("");
		GuiView.searchNameLabel.setText("");
		GuiView.searchCaptionLabel.setText("");
		GuiView.searchTagsLabel.setText("");
		GuiView.searchDateLabel.setText("");

		((HintTextField) GuiView.search).focusLost(null);

		switch (GuiView.menu.whichTabbed()) {

		case (0):
			GuiView.search.setText("search by tags");
			break;
		case (1):
			if (GuiView.singleAlbum) {
				GuiView.search.setText("search album");
			} else {
				GuiView.search.setText("search album");
			}
			break;
		case (2):
			GuiView.search.setText("search by date");
			break;

		}

		GuiView.searchLabel.setEnabled(false);

		GuiView.menu.requestFocus();

		GuiView.searching = false;
		GuiView.backLabel.setEnabled(false);
		// backLabel.setVisible(false);
	}

	// ################### Photo Double Clicked ##########################//
	protected static void photoDoubleClicked() {
		// System.out.println("Photo double clicked: "
		// + GuiView.menu.whichTabbed());

		// Which tab is selected on top
		if (GuiView.searching == true) {

		} else {
			switch (GuiView.menu.whichTabbed()) {

			case (0):

				GuiView.createPhotoViewer(GuiView.user.getAllPhotoPaths(),
						GuiView.allPhotosCollection.getSelection());
				break;
			case (1):

				// Check if were in single album mode and if you are just make a
				// photo viewer.
				if (GuiView.singleAlbum == false) {
					GuiView.createSingleAlbumView(false);
					GuiView.singleAlbum = true;
					GuiView.backLabel.setEnabled(true);
					GuiView.slider
							.setValue((int) (GuiView.singleAlbumCollection
									.getScale() * 100));
					GuiView.search.setText("search " + GuiView.album.getName());
					GuiView.albumEditButton.setVisible(false);
				} else {

					GuiView.createPhotoViewer(GuiView.album.getAllPhotoPaths(),
							GuiView.singleAlbumCollection.getSelection());

				}
				break;
			case (2):

				// Could do some clever stuff to get all photos for that year

				File[] temp = { GuiView.user.sortPhotos()[GuiView.yearsCollection
						.getSelection()].getFilePath() };

				System.out
						.println(GuiView.user.sortPhotos()[GuiView.yearsCollection
								.getSelection()].getFilePath().toString());
				GuiView.createPhotoViewer(temp, 0);

				break;

			}
		}
	}

	// ################### Photo Single Clicked ##########################//
	protected static void photoSingleClicked() {

		// System.out.println("Photo clicked");

		// Which tab is selected on top: photos(0), albums(0), years(0)
		if (GuiView.searching == true) {

			GuiView.photo = new PhotoEditorController(
					GuiView.searchResults[GuiView.searchCollection
							.getSelection()],
					null);

			GuiView.searchNameLabel.setText(GuiView.nameLabelConstant
					+ GuiView.photo.getPhotoFilename());
			GuiView.searchCaptionLabel.setText(GuiView.captionLabelConstant
					+ GuiView.photo.getPhotoCaption());
			GuiView.searchTagsLabel.setText(GuiView.tagsLabelConstant
					+ GuiView.photo.getTagsString());
			GuiView.searchDateLabel.setText(GuiView.dateLabelConstant
					+ GuiView.photo.getPhotoDate());

			GuiView.searchNameLabel.setVisible(true);
			GuiView.searchCaptionLabel.setVisible(true);
			GuiView.searchTagsLabel.setVisible(true);
			GuiView.searchMakeAlbumButton.setVisible(true);
			GuiView.searchDateLabel.setVisible(true);

		} else {

			switch (GuiView.menu.whichTabbed()) {

			case (0):

				GuiView.photo = new PhotoEditorController(
						GuiView.user.getPhotoByIndex(GuiView.allPhotosCollection
								.getSelection()), null);

				GuiView.photoNameLabel.setText(GuiView.nameLabelConstant
						+ GuiView.photo.getPhotoFilename());
				GuiView.photoCaptionLabel.setText(GuiView.captionLabelConstant
						+ GuiView.photo.getPhotoCaption());
				GuiView.photoTagsLabel.setText(GuiView.tagsLabelConstant
						+ GuiView.photo.getTagsString());
				GuiView.photoDateLabel.setText(GuiView.dateLabelConstant
						+ GuiView.photo.getPhotoDate());

				GuiView.photoNameLabel.setVisible(true);
				GuiView.photoCaptionLabel.setVisible(true);
				GuiView.photoTagsLabel.setVisible(true);
				GuiView.photosEditButton.setVisible(true);
				GuiView.photoDateLabel.setVisible(true);

				break;
			case (1):

				if (GuiView.singleAlbum == true) {

					if (GuiView.singleAlbumCollection.getThumbnails() != null) {
						GuiView.photo = new PhotoEditorController(
								GuiView.album.getPhotoByIndex(GuiView.singleAlbumCollection
										.getSelection()), null);

						GuiView.bottomDetailLayout.show(
								GuiView.bottomDetailPanel, "photos");
						// set the labels and stuff
						GuiView.photoNameLabel
								.setText(GuiView.nameLabelConstant
										+ GuiView.photo.getPhotoFilename());
						GuiView.photoCaptionLabel
								.setText(GuiView.captionLabelConstant
										+ GuiView.photo.getPhotoCaption());
						GuiView.photoTagsLabel
								.setText(GuiView.tagsLabelConstant
										+ GuiView.photo.getTagsString());
						GuiView.photoDateLabel
								.setText(GuiView.dateLabelConstant
										+ GuiView.photo.getPhotoDate());

						GuiView.photoNameLabel.setVisible(true);
						GuiView.photoCaptionLabel.setVisible(true);
						GuiView.photoTagsLabel.setVisible(true);
						GuiView.photosEditButton.setVisible(true);
						GuiView.photoDateLabel.setVisible(true);
					}

				} else {
					GuiView.album = new AlbumEditorController(
							GuiView.user.getAlbumByIndex(GuiView.albumsCollection
									.getSelection()), null);

					GuiView.albumNameLabel.setText("Album name: "
							+ GuiView.album.getName());
					GuiView.albumNumberLabel.setText("Photos: "
							+ GuiView.album.getAlbumSize());

					GuiView.albumDatesLabel.setText(GuiView.dateLabelConstant
							+ GuiView.album.getEarliest() + " - "
							+ GuiView.album.getLatest());
					// albumEditButton.setText("edit");

					GuiView.albumNameLabel.setVisible(true);
					GuiView.albumNumberLabel.setVisible(true);
					GuiView.albumEditButton.setVisible(true);
					GuiView.albumDatesLabel.setVisible(true);
				}
				break;
			case (2):

				GuiView.photo = new PhotoEditorController(
						GuiView.user.sortPhotos()[GuiView.yearsCollection
								.getSelection()],
						null);

				// Change details labels

				GuiView.yearNameLabel.setText(GuiView.nameLabelConstant
						+ GuiView.photo.getPhotoFilename());
				GuiView.yearCaptionLabel.setText(GuiView.captionLabelConstant
						+ GuiView.photo.getPhotoCaption());
				GuiView.yearTagsLabel.setText(GuiView.tagsLabelConstant
						+ GuiView.photo.getTagsString());
				GuiView.yearDateLabel.setText(GuiView.dateLabelConstant
						+ GuiView.photo.getPhotoDate());

				GuiView.yearNameLabel.setVisible(true);
				GuiView.yearCaptionLabel.setVisible(true);
				GuiView.yearTagsLabel.setVisible(true);
				GuiView.yearDateLabel.setVisible(true);

				break;

			}
		}
	}

	// ################### Delete Clicked ##########################//
	protected static void deleteClicked() {
		// System.out.println("Delete clicked");
		// Which tab is selected on top: photos(0), albums(0), years(0)
		switch (GuiView.menu.whichTabbed()) {

		case (0):

			deletePhoto();

			break;
		case (1):

			if (GuiView.singleAlbum) {

				deletePhoto();
			} else {
				deleteAlbum();

			}
			break;
		case (2):
			break;

		}

	}

	private static void deleteAlbum() {
		// delete the album

		GuiView.glass.setVisible(true);

		// name.setText(user.getFullName());

		GuiView.delete_album_window = new JWindow(GuiView.frame);
		GuiView.delete_album_window.setLayout(new BoxLayout(
				GuiView.delete_album_window.getContentPane(),
				BoxLayout.PAGE_AXIS));

		JPanel add_window_grid = new JPanel();
		add_window_grid.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel title = new JLabel("Remove "
				+ GuiView.user.getAlbumByIndex(
						GuiView.albumsCollection.getSelection()).getName()
				+ "?");

		title.setFont(new Font("Arial", Font.PLAIN, 25));
		title.setForeground(Color.GRAY);

		// Add title
		gbc.insets = new Insets(10, 10, 0, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		add_window_grid.add(Box.createRigidArea(new Dimension(40, 40)), gbc);

		add_window_grid.add(title, gbc);

		GuiView.delete_album_window.add(add_window_grid);
		GuiView.delete_album_window.add(Box.createVerticalStrut(20));

		// Create and customize the confirm/cancel buttons

		JLabel cancel = new JLabel("cancel", SwingConstants.CENTER);
		JLabel confirm = new JLabel("confirm", SwingConstants.CENTER);

		cancel.setFont(new Font("Arial", Font.PLAIN, 25));
		confirm.setFont(new Font("Arial", Font.PLAIN, 25));

		cancel.setBackground(new Color(240, 130, 130));
		cancel.setForeground(Color.DARK_GRAY);
		cancel.setOpaque(true);

		confirm.setBackground(new Color(130, 240, 130));
		confirm.setForeground(Color.DARK_GRAY);
		confirm.setOpaque(true);

		JPanel cancel_confirm_Outer = new JPanel(new GridLayout());
		cancel_confirm_Outer.add(cancel);
		cancel_confirm_Outer.add(confirm);

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				GuiView.delete_album_window.dispose();
				GuiView.glass.setVisible(false);

			}
		});

		confirm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (GuiView.user.removeUserAlbum(GuiView.album.getName())) {
					GuiView.delete_album_window.dispose();
					GuiView.glass.setVisible(false);
					GuiView.refreshPhotoCollections();
				}
			}

		});

		GuiView.delete_album_window.add(cancel_confirm_Outer);

		// Don't change!

		GuiView.delete_album_window.pack();
		GuiView.delete_album_window.setLocationRelativeTo(GuiView.frame);
		GuiView.delete_album_window.setVisible(true);
		GuiView.glass.requestFocus();

	}

	private static void deletePhoto() {

		GuiView.glass.setVisible(true);

		// name.setText(user.getFullName());

		GuiView.delete_photo_window = new JWindow(GuiView.frame);
		GuiView.delete_photo_window.setLayout(new BoxLayout(
				GuiView.delete_photo_window.getContentPane(),
				BoxLayout.PAGE_AXIS));

		JPanel add_window_grid = new JPanel();
		add_window_grid.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel title = new JLabel();

		if (GuiView.singleAlbum) {
			// remove from a specific album
			title.setText("Remove "
					+ GuiView.album.getPhotoByIndex(
							GuiView.singleAlbumCollection.getSelection())
							.getFilename() + "?");
		} else {

			title.setText("Remove "
					+ GuiView.user.getPhotoByIndex(
							GuiView.allPhotosCollection.getSelection())
							.getFilename() + "?");
			// remove from the user
			// if(//GuiView.user.)

		}

		title.setFont(new Font("Arial", Font.PLAIN, 25));
		title.setForeground(Color.GRAY);

		// Add title
		gbc.insets = new Insets(10, 10, 0, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		add_window_grid.add(Box.createRigidArea(new Dimension(40, 40)), gbc);

		add_window_grid.add(title, gbc);

		GuiView.delete_photo_window.add(add_window_grid);
		GuiView.delete_photo_window.add(Box.createVerticalStrut(20));

		// Create and customize the confirm/cancel buttons

		JLabel cancel = new JLabel("cancel", SwingConstants.CENTER);
		JLabel confirm = new JLabel("confirm", SwingConstants.CENTER);

		cancel.setFont(new Font("Arial", Font.PLAIN, 25));
		confirm.setFont(new Font("Arial", Font.PLAIN, 25));

		cancel.setBackground(new Color(240, 130, 130));
		cancel.setForeground(Color.DARK_GRAY);
		cancel.setOpaque(true);

		confirm.setBackground(new Color(130, 240, 130));
		confirm.setForeground(Color.DARK_GRAY);
		confirm.setOpaque(true);

		JPanel cancel_confirm_Outer = new JPanel(new GridLayout());
		cancel_confirm_Outer.add(cancel);
		cancel_confirm_Outer.add(confirm);

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				GuiView.delete_photo_window.dispose();
				GuiView.glass.setVisible(false);
			}
		});

		confirm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (GuiView.singleAlbum) {
					// remove from a specific album
					if (GuiView.album.removeAlbumPhoto(GuiView.album
							.getPhotoByIndex(
									GuiView.singleAlbumCollection
											.getSelection()).getFilename())) {
						GuiView.delete_photo_window.dispose();
						GuiView.glass.setVisible(false);
						GuiView.refreshPhotoCollections();
					}

				} else {

					// remove from the user
					// if(//GuiView.user.)
					if (GuiView.user.removePhoto(GuiView.user
							.getPhotoByIndex(GuiView.allPhotosCollection
									.getSelection()))) {
						GuiView.delete_photo_window.dispose();
						GuiView.glass.setVisible(false);
						GuiView.refreshPhotoCollections();

					}

				}
			}

		});

		GuiView.delete_photo_window.add(cancel_confirm_Outer);

		// Don't change!

		GuiView.delete_photo_window.pack();
		GuiView.delete_photo_window.setLocationRelativeTo(GuiView.frame);
		GuiView.delete_photo_window.setVisible(true);
		GuiView.glass.requestFocus();

	}

	// ################### Edit Clicked ##########################//
	/**
	 * Pops up an edit photo window
	 */
	public static void editPhotoClicked() {
		GuiView.glass.setVisible(true);

		JTextField tags = new HintTextField("add tag");
		JTextField caption = new HintTextField("");
		JTextField albumTF = new HintTextField("");

		tags.setPreferredSize(new Dimension(150, 25));
		caption.setPreferredSize(new Dimension(150, 25));
		albumTF.setPreferredSize(new Dimension(150, 25));

		JLabel cancel = new JLabel("cancel", SwingConstants.CENTER);
		JLabel confirm = new JLabel("confirm", SwingConstants.CENTER);
		// confirm.setEnabled(false);

		JLabel add = new JLabel("add", SwingConstants.CENTER);
		JLabel delete = new JLabel("delete", SwingConstants.CENTER);

		GuiView.edit_photo_window = new JWindow(GuiView.frame);
		GuiView.edit_photo_window
				.setLayout(new BoxLayout(GuiView.edit_photo_window
						.getContentPane(), BoxLayout.PAGE_AXIS));

		JPanel edit_window_grid = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel title = new JLabel("Edit Photo");
		title.setFont(new Font("Arial", Font.PLAIN, 25));
		title.setForeground(Color.GRAY);

		// title.setBackground(Color.BLUE);
		// title.setOpaque(true);

		// Add title
		gbc.insets = new Insets(10, 10, 0, 10);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		edit_window_grid.add(title, gbc);

		// add the labels
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;

		edit_window_grid.add(new JLabel("Caption: "), gbc);
		gbc.gridy++;
		edit_window_grid.add(new JLabel("Album: "), gbc);
		gbc.gridy++;

		gbc.anchor = GridBagConstraints.CENTER;
		edit_window_grid.add(new JLabel("Tags"), gbc);

		// add the fields
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		edit_window_grid.add(caption, gbc);
		gbc.gridy++;
		edit_window_grid.add(albumTF, gbc);

		if (GuiView.searching == true) {

		} else {

			switch (GuiView.menu.whichTabbed()) {

			case (0):

				GuiView.photo = new PhotoEditorController(
						GuiView.user.getPhotoByIndex(GuiView.allPhotosCollection
								.getSelection()), null);
				title.setText("Edit Photo");

				break;
			case (1):

				if (GuiView.singleAlbum) {

					GuiView.photo = new PhotoEditorController(
							GuiView.album.getPhotoByIndex(GuiView.singleAlbumCollection
									.getSelection()), null);

				} else {

					// never gets here

				}
				break;
			case (2):

				break;

			}
		}

		GuiView.edit_photo_window.add(edit_window_grid);

		// String[] selections = GuiView.photo.getPhotoTags();

		GuiView.tagsListModel = new DefaultListModel<String>();
		for (String tag : GuiView.photo.getTagStrings()) {

			GuiView.tagsListModel.addElement(tag);
		}
		JList<String> list = new JList<String>(GuiView.tagsListModel);

		list.setSelectedIndex(0);
		// System.out.println(list.getSelectedValue());
		GuiView.edit_photo_window.add(new JScrollPane(list));

		// GuiView.edit_photo_window.add(Box.createVerticalStrut(20));

		// add delete tags set

		delete.setFont(new Font("Arial", Font.PLAIN, 14));
		add.setFont(new Font("Arial", Font.PLAIN, 14));

		delete.setBackground(new Color(215, 20, 20));
		delete.setForeground(Color.white);
		delete.setOpaque(true);

		add.setBackground(new Color(100, 158, 224));
		add.setForeground(Color.white);
		add.setOpaque(true);

		JPanel add_delete_Outer = new JPanel(new GridLayout());
		add_delete_Outer.add(delete);
		add_delete_Outer.add(add);

		delete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {

				if (list.getSelectedIndex() > -1) {
					int error = GuiView.photo.removeTag(GuiView.tagsListModel
							.elementAt(list.getSelectedIndex()));

					// System.out.println(GuiView.tagsListModel.elementAt(list
					// .getSelectedIndex()));
					if (error > 0) {
						// worked
						GuiView.tagsListModel.remove(list.getSelectedIndex());

					} else {
						// failed

						// System.out.println("failed!");

					}
				}
			}
		});

		add.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (!tags.getText().equals("")) {
					int error = GuiView.photo.addTag(tags.getText());
					if (error != 1) {

						tags.setText("");
						GuiView.badInput(tags);

					} else {
						GuiView.tagsListModel.addElement(tags.getText());
						tags.setText("");
						GuiView.goodInput(tags);
					}

					// System.out.println("Tags: " +
					// GuiView.photo.getTagsString());

				} else {

					GuiView.badInput(tags);

				}
			}
		});

		GuiView.edit_photo_window.add(tags);

		// GuiView.edit_photo_window.add(Box.createVerticalStrut(10));

		GuiView.edit_photo_window.add(add_delete_Outer);

		GuiView.edit_photo_window.add(Box.createVerticalStrut(20));

		// Create and customize the confirm/cancel buttons

		cancel.setFont(new Font("Arial", Font.PLAIN, 25));
		confirm.setFont(new Font("Arial", Font.PLAIN, 25));

		cancel.setBackground(new Color(240, 130, 130));
		cancel.setForeground(Color.DARK_GRAY);
		cancel.setOpaque(true);

		confirm.setBackground(new Color(130, 240, 130));
		confirm.setForeground(Color.DARK_GRAY);
		confirm.setOpaque(true);

		JPanel cancel_confirm_Outer = new JPanel(new GridLayout());
		cancel_confirm_Outer.add(cancel);
		cancel_confirm_Outer.add(confirm);

		cancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				GuiView.edit_photo_window.dispose();
				GuiView.glass.setVisible(false);

			}
		});

		confirm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {

				if (caption.getText().equals("")
						&& albumTF.getText().equals("")) {
					// no changes made, do nothing
					GuiView.edit_photo_window.dispose();
					GuiView.glass.setVisible(false);
				} else {
					if (caption.getText().equals("")) {
						// only album changed

						AlbumEditorController oldAlbum = GuiView.user
								.editAlbum(GuiView.photo.getAlbum());
						AlbumEditorController newAlbum = GuiView.user
								.editAlbum(albumTF.getText());

						if (oldAlbum != null && newAlbum != null
								&& oldAlbum != newAlbum) {
							GuiView.user.movePhoto(
									GuiView.photo.getPhotoFilename(), oldAlbum,
									newAlbum);
							GuiView.edit_photo_window.dispose();
							GuiView.glass.setVisible(false);

						} else {

							albumTF.setText("");
							GuiView.badInput(albumTF);
						}

					} else if (albumTF.getText().equals("")) {
						// only caption changed
						GuiView.photo.setPhotoCaption(caption.getText());
						GuiView.edit_photo_window.dispose();
						GuiView.glass.setVisible(false);

					} else {
						// both changed
						AlbumEditorController oldAlbum = GuiView.user
								.editAlbum(GuiView.photo.getAlbum());
						AlbumEditorController newAlbum = GuiView.user
								.editAlbum(albumTF.getText());

						if (oldAlbum != null && newAlbum != null) {
							GuiView.user.movePhoto(
									GuiView.photo.getPhotoFilename(), oldAlbum,
									newAlbum);
							GuiView.photo.setPhotoCaption(caption.getText());

							GuiView.edit_photo_window.dispose();
							GuiView.glass.setVisible(false);

						} else {

							albumTF.setText("");
							GuiView.badInput(albumTF);
						}

					}

				}

			}
		});

		list.requestFocus();

		GuiView.edit_photo_window.add(cancel_confirm_Outer);

		GuiView.edit_photo_window.pack();
		GuiView.edit_photo_window.setLocationRelativeTo(GuiView.frame);
		GuiView.edit_photo_window.setVisible(true);

	}

	/**
	 * Pops up an edit album window
	 */
	public static void editAlbumClicked() {

		if (GuiView.singleAlbum) {

			editPhotoClicked();

		} else {

			// make the edit album window!
			GuiView.glass.setVisible(true);

			JTextField name = new HintTextField(GuiView.album.getName());
			JTextField caption = new HintTextField(GuiView.album.getCaption());

			name.setPreferredSize(new Dimension(175, 25));
			caption.setPreferredSize(new Dimension(175, 25));

			GuiView.edit_album_window = new JWindow(GuiView.frame);
			GuiView.edit_album_window.setLayout(new BoxLayout(
					GuiView.edit_album_window.getContentPane(),
					BoxLayout.PAGE_AXIS));

			JPanel add_window_grid = new JPanel();
			add_window_grid.setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();

			JLabel title = new JLabel("Edit");
			title.setFont(new Font("Arial", Font.PLAIN, 25));
			title.setForeground(Color.GRAY);

			JLabel albumName = new JLabel(GuiView.album.getName());
			albumName.setFont(new Font("Arial", Font.PLAIN, 25));
			albumName.setForeground(Color.GRAY);

			// Add title
			gbc.insets = new Insets(10, 10, 0, 10);
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.LINE_END;
			// add_window_grid
			// .add(Box.createRigidArea(new Dimension(40, 40)), gbc);

			add_window_grid.add(title, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.LINE_START;
			add_window_grid.add(albumName);

			gbc.anchor = GridBagConstraints.LINE_END;
			gbc.gridx = 0;
			gbc.gridy++;
			add_window_grid.add(new JLabel("Name: "), gbc);
			gbc.gridy++;
			add_window_grid.add(new JLabel("Caption: "), gbc);

			gbc.gridy = 1;
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.LINE_START;
			add_window_grid.add(name, gbc);
			gbc.gridy++;
			add_window_grid.add(caption, gbc);

			GuiView.edit_album_window.add(add_window_grid);
			GuiView.edit_album_window.add(Box.createVerticalStrut(20));

			// Create and customize the confirm/cancel buttons

			JLabel cancel = new JLabel("cancel", SwingConstants.CENTER);
			JLabel confirm = new JLabel("confirm", SwingConstants.CENTER);
			cancel.setFont(new Font("Arial", Font.PLAIN, 25));
			confirm.setFont(new Font("Arial", Font.PLAIN, 25));

			cancel.setBackground(new Color(240, 130, 130));
			cancel.setForeground(Color.DARK_GRAY);
			cancel.setOpaque(true);

			confirm.setBackground(new Color(130, 240, 130));
			confirm.setForeground(Color.DARK_GRAY);
			confirm.setOpaque(true);

			JPanel cancel_confirm_Outer = new JPanel(new GridLayout());
			cancel_confirm_Outer.add(cancel);
			cancel_confirm_Outer.add(confirm);

			cancel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					GuiView.edit_album_window.dispose();
					GuiView.glass.setVisible(false);

				}
			});

			confirm.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {

					if (!name.getText().equals("")) {
						GuiView.album.setCaption(caption.getText());
						GuiView.album.setName(name.getText());

						GuiView.edit_album_window.dispose();
						GuiView.glass.setVisible(false);

					} else {

						GuiView.badInput(name);

					}

				}
			});

			GuiView.edit_album_window.add(cancel_confirm_Outer);

			// Don't change!

			GuiView.edit_album_window.pack();
			GuiView.edit_album_window.setLocationRelativeTo(GuiView.frame);
			GuiView.edit_album_window.setVisible(true);

		}

	}

	/**
	 * Pops up a make album window
	 */
	public static void makeAlbumButtonClicked() {
		// when searching, if clicked it creates on album with the search
		// results

		addAlbum();

	}
}
// ################### Clicked ##########################//
