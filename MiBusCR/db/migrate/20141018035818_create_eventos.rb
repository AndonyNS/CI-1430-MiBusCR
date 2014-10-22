class CreateEventos < ActiveRecord::Migration
  def change
    create_table :eventos do |t|
      t.string :nombre
      t.string :descripcion
      t.integer :tipo
      t.datetime :dia_y_hora
      t.string :lugar
      t.float  :latitud
      t.float  :longitud

      t.timestamps
    end
  end
end
