## Java 8

Ohne besondere Settings:

* bestehende Indentierung bleibt erhalten
* neue Knoten sind inline
* CDATA ebenso
* `xml:space="preserve"` macht keinen Unterschied

Mit `OutputKeys.INDENT=yes`, `indent-amount=4`:

* bestehende Indentierung bleibt erhalten
* neue Knoten werden entsprechend Einstellungen eingerückt
* CDATA wird nicht eingerückt
* `xml:space="preserve"` macht keinen Unterschied

## Java 9

Ohne besondere Settings:

* bestehende Indentierung bleibt erhalten
* neue Knoten sind inline
* CDATA ebenso
* `xml:space="preserve"` macht keinen Unterschied

Mit `OutputKeys.INDENT=yes`, `indent-amount=4`:

* gesamtes Dokument neu engerückt (?)
* neue Knoten ebenso
* CDATA ebenso (!)
* mit `xml:space="preserve"` wie ohne besondere Settings
