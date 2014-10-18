class CreateEventos < ActiveRecord::Migration
  def change
    create_table :eventos do |t|
      t.string :Descripcion
      t.integer :Tipo
      t.time :Hora
      t.string :Lugar

      t.timestamps
    end
  end
end
