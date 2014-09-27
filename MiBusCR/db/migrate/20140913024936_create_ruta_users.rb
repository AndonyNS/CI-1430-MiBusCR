class CreateRutaUsers < ActiveRecord::Migration
  def change
    create_table :ruta_users do |t|
      t.belongs_to :ruta
      t.belongs_to :user

      t.timestamps
    end
  end
end
