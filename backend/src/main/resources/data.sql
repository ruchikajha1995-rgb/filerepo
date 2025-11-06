
insert into nodes (id, version, name, type, parent_id, position) values
  (1, 0, 'Documents', 'FOLDER', null, 0),
  (2, 0, 'Pictures',  'FOLDER', null, 1);
insert into nodes (id, version, name, type, parent_id, position) values
  (3, 0, 'Resume.pdf', 'FILE', 1, 0),
  (4, 0, 'CoverLetter.docx', 'FILE', 1, 1),
  (5, 0, 'Holidays', 'FOLDER', 2, 0),
  (6, 0, 'beach.png', 'FILE', 5, 0);
insert into node_tags (id, node_id, k, v) values
  (1, 3, 'category','career'),
  (2, 6, 'camera','pixel');
